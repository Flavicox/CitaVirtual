package com.flavicox.citavirtual.bd.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "drprog",
    foreignKeys = [ForeignKey(
        entity = DoctorEntidad::class,
        parentColumns = ["id"],
        childColumns = ["doctorId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("doctorId")]
)
data class ProgramacionDoctorEntidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val doctorId: Int,
    val fecha: Long,
    val hora: String
)