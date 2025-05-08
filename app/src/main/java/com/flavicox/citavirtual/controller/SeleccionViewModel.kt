package com.flavicox.citavirtual.controller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flavicox.citavirtual.model.Doctor
import com.flavicox.citavirtual.model.DrProg
import com.flavicox.citavirtual.model.Programacion
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class SeleccionViewModel : ViewModel() {

    private val _drProgsFiltrados = MutableLiveData<List<DrProg>>()
    val drProgsFiltrados: LiveData<List<DrProg>> = _drProgsFiltrados
    private val _todasProgramaciones = MutableLiveData<List<DrProg>>()
    val todasProgramaciones: LiveData<List<DrProg>> = _todasProgramaciones


    private val datosMock: List<DrProg>

    init {
        val generadas = generarDoctoresParaProximosDias()
        datosMock = generadas
        _todasProgramaciones.value = generadas.sortedBy { it.fecha }

        // Registrar los horarios en el repositorio
        CitaRepository.agregarHorarios(generadas)
    }

    private fun generarDoctoresParaProximosDias(): List<DrProg> {
        val especialidades = listOf("Pediatría", "Medicina General", "Dermatología")
        val nombres = listOf("Luis", "Ana", "Mario", "Valeria", "Carlos", "Sofía", "Fernando", "Lucía", "Juan", "Camila")
        val apellidos = listOf("Martínez", "Ramírez", "Fernández", "Gonzales", "Pérez", "Sánchez", "Castro", "López")

        val random = Random()
        val lista = mutableListOf<DrProg>()

        repeat(30) { // Crear 30 doctores aleatorios
            val nombre = nombres.random()
            val apPaterno = apellidos.random()
            val apMaterno = apellidos.random()
            val especialidad = especialidades.random()

            val doctor = Doctor(nombre, apPaterno, apMaterno, especialidad)

            // Día aleatorio entre hoy y 5 días después
            val fecha = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, random.nextInt(5)) // 0 a 4 días después
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            // Hora aleatoria entre 8:00 AM y 4:00 PM
            val horaAleatoria = random.nextInt(9) + 8 // entre 8 y 16
            val horaTexto = String.format("%02d:00 %s", horaAleatoria % 12, if (horaAleatoria < 12) "AM" else "PM")

            lista.add(DrProg(doctor, fecha, horaTexto))
        }

        return lista
    }

    fun filtrarProgramaciones(sede: String, especialidad: String, fecha: Date) {
        _drProgsFiltrados.value = datosMock.filter { drProg ->
            val coincideSede = obtenerSedeDeDoctor(drProg.doctor) == sede
            val coincideEspecialidad = drProg.doctor.especialidad.equals(especialidad, ignoreCase = true)
            val mismaFecha = esMismaFecha(drProg.fecha, fecha)

            coincideSede && coincideEspecialidad && mismaFecha
        }
    }

    private fun obtenerSedeDeDoctor(doctor: Doctor): String {
        return when (doctor.nombre) {
            "Luis" -> "Sede A"
            "Ana" -> "Sede B"
            "Mario" -> "Sede A"
            else -> "Sede A"
        }
    }

    private fun esMismaFecha(fecha1: Date, fecha2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = fecha1 }
        val cal2 = Calendar.getInstance().apply { time = fecha2 }

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }

    fun eliminarProgramacion(drProg: DrProg) {
        val nuevaLista = _todasProgramaciones.value?.toMutableList()
        nuevaLista?.remove(drProg)
        _todasProgramaciones.value = nuevaLista?.sortedBy { it.fecha }

        // Si estaba filtrado también, actualizar filtro
        val filtrados = _drProgsFiltrados.value?.toMutableList()
        filtrados?.remove(drProg)
        _drProgsFiltrados.value = filtrados
    }

}