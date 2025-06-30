package com.flavicox.citavirtual.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.flavicox.citavirtual.model.Doctor
import com.flavicox.citavirtual.model.DrProg
import com.flavicox.citavirtual.util.aEntidad
import com.flavicox.citavirtual.util.aModelo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Random

class SeleccionViewModel(application: Application) : AndroidViewModel(application) {

    /* ---------- acceso a BD ---------- */
    private val repositorio = CitaRepositorio(application)

    /* ---------- flujos para la UI ---------- */
    private val _drProgsFiltrados = MutableStateFlow<List<DrProg>>(emptyList())
    val drProgsFiltrados: StateFlow<List<DrProg>> = _drProgsFiltrados

    /**
     * Todas las programaciones almacenadas en Room, convertidas a tu
     * clase de dominio `DrProg` (con un “doctor mock” mientras no persistas doctores).
     */
    val todasProgramaciones: StateFlow<List<DrProg>> =
        combine(
            repositorio.horariosDisponibles,
            repositorio.doctores
        ) { listaProgramaciones, listaDoctores ->
            val mapaDoctores = listaDoctores.associateBy { it.id }
            listaProgramaciones.mapNotNull { entidad ->
                val doctorEntidad = mapaDoctores[entidad.doctorId]
                doctorEntidad?.let { entidad.aModelo(it.aModelo()) }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /* ---------- carga inicial de datos simulados ---------- */
    init {
        viewModelScope.launch {
            val doctores = generarDoctores()
            val programaciones = generarProgramacionesParaDoctores(doctores)

            repositorio.agregarDoctores(doctores)
            repositorio.agregarHorarios(programaciones.map { it.aEntidad() })
        }
    }

    /* ---------- generación de mocks ---------- */
    private fun generarDoctores(): List<Doctor> {
        val especialidades = listOf("Pediatría", "Medicina General", "Dermatología")
        val nombres = listOf("Luis", "Ana", "Mario", "Valeria", "Carlos",
            "Sofía", "Fernando", "Lucía", "Juan", "Camila")
        val apellidos = listOf("Martínez", "Ramírez", "Fernández", "Gonzales",
            "Pérez", "Sánchez", "Castro", "López")

        val random = Random()
        return List(10) { i ->
            Doctor(
                id = i + 1,
                nombre = nombres[i],
                apPaterno = apellidos.random(),
                apMaterno = apellidos.random(),
                especialidad = especialidades.random(),
                telefono = "9${(10000000..99999999).random()}"
            )
        }
    }

    private fun generarProgramacionesParaDoctores(doctores: List<Doctor>): List<DrProg> {
        val random = Random()
        val lista = mutableListOf<DrProg>()
        var autoIdProg = 1

        doctores.forEach { doctor ->
            repeat(3) {
                val fecha = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, random.nextInt(5)) // 0–4 días adelante
                    set(Calendar.HOUR_OF_DAY, 0); clear(Calendar.MINUTE)
                    clear(Calendar.SECOND); clear(Calendar.MILLISECOND)
                }.time

                val horaAleatoria = random.nextInt(9) + 8 // 8–16 h
                val horaTexto = String.format(
                    "%02d:00 %s",
                    horaAleatoria % 12,
                    if (horaAleatoria < 12) "AM" else "PM"
                )

                lista.add(
                    DrProg(
                        id = autoIdProg++,
                        doctor = doctor,
                        fecha = fecha,
                        hora = horaTexto
                    )
                )
            }
        }

        return lista
    }

    /* ---------- filtros ---------- */
    fun filtrarProgramaciones(sede: String, especialidad: String, fecha: Date) {
        val base = todasProgramaciones.value
        _drProgsFiltrados.value = base.filter { prog ->
            val coincideSede        = obtenerSedeDeDoctor(prog.doctor) == sede
            val coincideEspecialidad = prog.doctor.especialidad.equals(especialidad, true)
            val mismaFecha          = esMismaFecha(prog.fecha, fecha)
            coincideSede && coincideEspecialidad && mismaFecha
        }
    }

    /* ---------- utilidades ---------- */
    private fun obtenerSedeDeDoctor(doctor: Doctor): String = when (doctor.nombre) {
        "Luis"  -> "Sede A"
        "Ana"   -> "Sede B"
        "Mario" -> "Sede A"
        else    -> "Sede A"
    }

    private fun esMismaFecha(f1: Date, f2: Date): Boolean {
        val c1 = Calendar.getInstance().apply { time = f1 }
        val c2 = Calendar.getInstance().apply { time = f2 }
        return c1[Calendar.YEAR]  == c2[Calendar.YEAR]  &&
                c1[Calendar.MONTH] == c2[Calendar.MONTH] &&
                c1[Calendar.DAY_OF_MONTH] == c2[Calendar.DAY_OF_MONTH]
    }

    /* ---------- eliminar programación ---------- */
    fun eliminarProgramacion(drProg: DrProg) = viewModelScope.launch {
        repositorio.eliminarHorarioReservado(drProg.aEntidad())
    }
}
