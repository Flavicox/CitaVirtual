package com.flavicox.citavirtual.bd.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "doctor")
data class DoctorEntidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apPaterno: String,
    val apMaterno: String,
    val especialidad: String,
    val telefono: String
)
