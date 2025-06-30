package com.flavicox.citavirtual.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.flavicox.citavirtual.R
import com.flavicox.citavirtual.bd.entity.ProgramacionDoctorEntidad
import com.flavicox.citavirtual.controller.CitaRepositorio
import com.flavicox.citavirtual.databinding.ActivityCitaBinding
import com.flavicox.citavirtual.model.Cita
import com.flavicox.citavirtual.model.Doctor
import com.flavicox.citavirtual.model.DrProg
import com.flavicox.citavirtual.model.Paciente
import com.flavicox.citavirtual.util.aEntidad
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Date

class CitaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitaBinding
    private lateinit var doctor: Doctor
    private lateinit var fecha: Date
    private lateinit var hora: String

    private lateinit var repositorio: CitaRepositorio
    private lateinit var programacionSeleccionada: ProgramacionDoctorEntidad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repositorio = CitaRepositorio(this)

        /* ---------- Datos que vienen del Intent ---------- */
        doctor = intent.getSerializableExtra("doctor") as Doctor
        fecha  = Date(intent.getLongExtra("fecha", 0L))
        hora   = intent.getStringExtra("hora") ?: "00:00"

        /*  Importante: construimos la entidad de programación
            con la misma PK (id) que se envió desde la lista */
        val progId    = intent.getIntExtra("progId", 0)
        programacionSeleccionada = ProgramacionDoctorEntidad(
            id        = progId,
            doctorId  = doctor.id,
            fecha     = fecha.time,
            hora      = hora
        )

        binding.tvDatosDoctor.text =
            "Dr. ${doctor.nombre} ${doctor.apPaterno} - ${doctor.especialidad} a las $hora"

        binding.btnConfirmarCita.setOnClickListener { registrarCita() }
    }

    private fun registrarCita() {
        val sintomas = binding.etSintomas.text.toString().trim()
        val diagnostico = binding.etDiagnostico.text.toString().trim()
        val alergias = binding.etAlergias.text.toString().trim()
        val acepta = binding.cbAceptarTerminos.isChecked

        if (!acepta) {
            Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show()
            return
        }

        val paciente = Paciente("Juan", "Pérez", "Sánchez") // Simulado

        val cita = Cita(
            id = 0, // se autogenerará en BD
            fecha = fecha,
            sintomas = sintomas,
            diagnostico = diagnostico,
            alergias = alergias,
            aceptaTerm = acepta,
            doctor = doctor,
            paciente = paciente
        )

        /* ---------- Guardar en BD ---------- */
        lifecycleScope.launch {
            repositorio.guardarCita(
                cita = cita.aEntidad(),          // mapeo Modelo → Entidad
                horario = programacionSeleccionada
            )

            Snackbar.make(binding.root, "Cita registrada exitosamente", Snackbar.LENGTH_LONG).show()

            binding.root.postDelayed({ finish() }, 1500)
        }
    }

}