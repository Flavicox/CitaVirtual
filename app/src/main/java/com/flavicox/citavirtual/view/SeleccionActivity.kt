package com.flavicox.citavirtual.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flavicox.citavirtual.R
import com.flavicox.citavirtual.controller.SeleccionViewModel
import com.flavicox.citavirtual.databinding.ActivitySeleccionBinding
import com.flavicox.citavirtual.view.adapter.DoctorAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SeleccionActivity : AppCompatActivity() {
    private lateinit var adapter: DoctorAdapter

    private lateinit var binding: ActivitySeleccionBinding
    private lateinit var viewModel: SeleccionViewModel
    private var fechaSeleccionada: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewBinding
        binding = ActivitySeleccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this)[SeleccionViewModel::class.java]

        // Configurar RecyclerView
        adapter = DoctorAdapter(emptyList(), this) { drProgSeleccionado -> }


        binding.recyclerDoctores.adapter = adapter
        binding.recyclerDoctores.setHasFixedSize(true)

        binding.recyclerDoctores.layoutManager = LinearLayoutManager(this)

        // Cargar opciones iniciales
        inicializarSpinners()
        configurarSelectorFecha()

        // Acción cuando se cambia algo y se quiere filtrar
        binding.btnSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        // Observar cambios del ViewModel
        viewModel.drProgsFiltrados.observe(this) { lista ->
            adapter.actualizarLista(lista)
        }

        viewModel.todasProgramaciones.observe(this) { listaCompleta ->
            adapter.actualizarLista(listaCompleta)
        }
    }

    private fun inicializarSpinners() {
        val sedes = listOf("Sede A", "Sede B")
        val especialidades = listOf("Pediatría", "Medicina General", "Dermatología")

        // Adaptadores
        val sedeAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, sedes)
        val especialidadAdapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_item, especialidades)

        sedeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        especialidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerSede.adapter = sedeAdapter
        binding.spinnerEspecialidad.adapter = especialidadAdapter
    }

    private fun configurarSelectorFecha() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        binding.btnSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        // También puedes predefinir una fecha si quieres
        binding.tvFechaSeleccionada.text = "Fecha no seleccionada"
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, year, month, day ->
            val calendarSeleccionado = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }

            fechaSeleccionada = calendarSeleccionado.time
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.tvFechaSeleccionada.text = formato.format(fechaSeleccionada!!)

            // Filtro al seleccionar fecha
            val sede = binding.spinnerSede.selectedItem.toString()
            val especialidad = binding.spinnerEspecialidad.selectedItem.toString()
            viewModel.filtrarProgramaciones(sede, especialidad, fechaSeleccionada!!)
        }, anio, mes, dia)

        datePicker.show()
    }


}