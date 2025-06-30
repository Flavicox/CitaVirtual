package com.flavicox.citavirtual.bd.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cita",
    foreignKeys = [
        ForeignKey(entity = DoctorEntidad::class, parentColumns = ["id"], childColumns = ["doctorId"]),
    ],
    indices = [Index("doctorId")]
)
data class CitaEntidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorId: Int,
    val fecha: Long,
    val sintomas: String,
    val diagnostico: String,
    val alergias: String,
    val aceptaTerm: Boolean
)
