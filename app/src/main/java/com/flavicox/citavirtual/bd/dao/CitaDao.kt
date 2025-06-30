package com.flavicox.citavirtual.bd.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.flavicox.citavirtual.bd.entity.CitaEntidad
import com.flavicox.citavirtual.bd.entity.ProgramacionDoctorEntidad
import kotlinx.coroutines.flow.Flow

@Dao
interface CitaDao {

    // Citas
    @Insert
    suspend fun insertarCita(cita: CitaEntidad)
    @Query("SELECT * FROM cita ORDER BY fecha DESC")
    fun observarCitas(): Flow<List<CitaEntidad>>

    // Programaciones
    @Insert
    suspend fun insertarProgramaciones(lista: List<ProgramacionDoctorEntidad>)
    @Delete
    suspend fun eliminarProgramacion(programacion: ProgramacionDoctorEntidad)
    @Query("SELECT * FROM drprog")
    fun observarProgramaciones(): Flow<List<ProgramacionDoctorEntidad>>
}