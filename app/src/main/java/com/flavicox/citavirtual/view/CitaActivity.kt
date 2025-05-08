package com.flavicox.citavirtual.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flavicox.citavirtual.R
import com.flavicox.citavirtual.controller.CitaRepository
import com.flavicox.citavirtual.databinding.ActivityCitaBinding
import com.flavicox.citavirtual.model.Cita
import com.flavicox.citavirtual.model.Doctor
import com.flavicox.citavirtual.model.DrProg
import com.flavicox.citavirtual.model.Paciente
import com.google.android.material.snackbar.Snackbar
import java.util.Date

class CitaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCitaBinding
    private lateinit var doctor: Doctor
    private lateinit var fecha: Date
    private lateinit var hora: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Intent
        doctor = intent.getSerializableExtra("doctor") as Doctor
        fecha = Date(intent.getLongExtra("fecha", 0L))
        hora = intent.getStringExtra("hora") ?: "00:00"

        binding.tvDatosDoctor.text = "Dr. ${doctor.nombre} ${doctor.apPaterno} - ${doctor.especialidad} a las $hora"

        binding.btnConfirmarCita.setOnClickListener {
            registrarCita()
        }
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
            fecha = fecha,
            sintomas = sintomas,
            diagnostico = diagnostico,
            alergias = alergias,
            aceptaTerm = acepta,
            doctor = doctor,
            paciente = paciente
        )

        CitaRepository.guardarCita(cita)
        CitaRepository.eliminarHorarioReservado(DrProg(doctor, fecha, hora))

        Snackbar.make(binding.root, "Cita registrada exitosamente", Snackbar.LENGTH_LONG).show()
        println("Cita: $cita")

        binding.root.postDelayed({
            finish()
        }, 1500)
    }

}