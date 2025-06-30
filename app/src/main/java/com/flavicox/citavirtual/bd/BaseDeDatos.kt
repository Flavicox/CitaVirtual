package com.flavicox.citavirtual.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.flavicox.citavirtual.bd.dao.CitaDao
import com.flavicox.citavirtual.bd.dao.DoctorDao
import com.flavicox.citavirtual.bd.entity.CitaEntidad
import com.flavicox.citavirtual.bd.entity.DoctorEntidad
import com.flavicox.citavirtual.bd.entity.ProgramacionDoctorEntidad

@Database(
    entities = [
        DoctorEntidad::class,
        ProgramacionDoctorEntidad::class,
        CitaEntidad::class
    ],
    version = 1,
    exportSchema = false
)
abstract class BaseDeDatos : RoomDatabase() {

    abstract fun citaDao(): CitaDao
    abstract fun doctorDao(): DoctorDao

    companion object {
        @Volatile
        private var INSTANCIA: BaseDeDatos? = null

        fun obtenerInstancia(contexto: Context): BaseDeDatos {
            return INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDeDatos::class.java,
                    "cita_virtual_bd"
                ).build().also { INSTANCIA = it }
            }
        }
    }
}