package com.flavicox.citavirtual.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.flavicox.citavirtual.controller.SeleccionViewModel
import com.flavicox.citavirtual.databinding.ActivitySeleccionBinding
import com.flavicox.citavirtual.view.adapter.DoctorAdapter
import com.flavicox.citavirtual.view.components.FechaDialogoFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SeleccionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionBinding
    private lateinit var viewModel: SeleccionViewModel
    private lateinit var adapter: DoctorAdapter

    /* Últimos filtros seleccionados */
    private var fechaSeleccionada: Date? = null
    private var sedeSeleccionada: String = "Sede A"
    private var especialidadSeleccionada: String = "Pediatría"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        /* ---------- ViewBinding ---------- */
        binding = ActivitySeleccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* ---------- ViewModel ---------- */
        viewModel = ViewModelProvider(this)[SeleccionViewModel::class.java]

        /* ---------- RecyclerView ---------- */
        adapter = DoctorAdapter(emptyList(), this) { drProg ->
            // Aquí lanzarías la actividad para confirmar la cita
        }
        binding.recyclerDoctores.apply {
            adapter = this@SeleccionActivity.adapter
            layoutManager = LinearLayoutManager(this@SeleccionActivity)
            setHasFixedSize(true)
        }

        /* ---------- Spinners ---------- */
        inicializarSpinners()

        /* ---------- Botón fecha ---------- */
        binding.tvFechaSeleccionada.text = "Fecha no seleccionada"
        binding.btnSeleccionarFecha.setOnClickListener { abrirDialogoFecha() }

        /* ---------- Colectar flujos del ViewModel ---------- */
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Todas las programaciones si no hay filtros
                launch {
                    viewModel.todasProgramaciones.collect { lista ->
                        if (fechaSeleccionada == null) {
                            adapter.actualizarLista(lista)
                        }
                    }
                }

                // Solo filtradas si ya se seleccionó una fecha
                launch {
                    viewModel.drProgsFiltrados.collect { listaFiltrada ->
                        if (fechaSeleccionada != null) {
                            adapter.actualizarLista(listaFiltrada)
                        }
                    }
                }
            }
        }
    }

    /* -------------------- Spinners -------------------- */
    private fun inicializarSpinners() {
        val sedes = listOf("Sede A", "Sede B")
        val especialidades = listOf("Pediatría", "Medicina General", "Dermatología")

        val sedeAdapter = android.widget.ArrayAdapter(this,
            android.R.layout.simple_spinner_item, sedes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        val espAdapter = android.widget.ArrayAdapter(this,
            android.R.layout.simple_spinner_item, especialidades).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spinnerSede.adapter = sedeAdapter
        binding.spinnerEspecialidad.adapter = espAdapter

        /* Detectar cambios para filtrar inmediatamente */
        binding.spinnerSede.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                sedeSeleccionada = sedes[position]
                filtrarSiHayFecha()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
        binding.spinnerEspecialidad.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                especialidadSeleccionada = especialidades[position]
                filtrarSiHayFecha()
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
    }

    /* -------------------- DatePickerDialogFragment -------------------- */
    private fun abrirDialogoFecha() {
        FechaDialogoFragment { fecha ->
            fechaSeleccionada = fecha
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.tvFechaSeleccionada.text = formato.format(fecha)

            viewModel.filtrarProgramaciones(
                sedeSeleccionada,
                especialidadSeleccionada,
                fecha
            )
        }.show(supportFragmentManager, "fechaDialogo")
    }

    private fun filtrarSiHayFecha() {
        fechaSeleccionada?.let { fecha ->
            viewModel.filtrarProgramaciones(
                sedeSeleccionada,
                especialidadSeleccionada,
                fecha
            )
        }
    }
}
