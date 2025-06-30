package com.flavicox.citavirtual.controller

import android.content.Context
import com.flavicox.citavirtual.bd.BaseDeDatos
import com.flavicox.citavirtual.bd.entity.CitaEntidad
import com.flavicox.citavirtual.bd.entity.DoctorEntidad
import com.flavicox.citavirtual.bd.entity.ProgramacionDoctorEntidad
import com.flavicox.citavirtual.model.Cita
import com.flavicox.citavirtual.model.Doctor
import com.flavicox.citavirtual.model.DrProg
import kotlinx.coroutines.flow.Flow

class CitaRepositorio(private val contexto: Context ) {

    private val dao = BaseDeDatos.obtenerInstancia(contexto).citaDao()

    // Obtener lista de citas registradas
    val citas: Flow<List<CitaEntidad>> = dao.observarCitas()

    // Obtener horarios disponibles (programaciones)
    val horariosDisponibles: Flow<List<ProgramacionDoctorEntidad>> = dao.observarProgramaciones()

    // Guardar una cita nueva y eliminar el horario reservado
    suspend fun guardarCita(cita: CitaEntidad, horario: ProgramacionDoctorEntidad) {
        dao.insertarCita(cita)
        dao.eliminarProgramacion(horario)
    }

    // Precargar lista de horarios (mock inicial)
    suspend fun agregarHorarios(mocks: List<ProgramacionDoctorEntidad>) {
        dao.insertarProgramaciones(mocks)
    }

    // Eliminar un horario específico manualmente
    suspend fun eliminarHorarioReservado(horario: ProgramacionDoctorEntidad) {
        dao.eliminarProgramacion(horario)
    }

    val doctores: Flow<List<DoctorEntidad>> =
        BaseDeDatos.obtenerInstancia(contexto).doctorDao().observarDoctores()

    suspend fun agregarDoctores(lista: List<Doctor>) {
        val entidades = lista.map { doctor ->
            DoctorEntidad(
                id = doctor.id,
                nombre = doctor.nombre,
                apPaterno = doctor.apPaterno,
                apMaterno = doctor.apMaterno,
                especialidad = doctor.especialidad,
                telefono = doctor.telefono
            )
        }
        BaseDeDatos.obtenerInstancia(contexto).doctorDao().insertarTodos(entidades)
    }
}

