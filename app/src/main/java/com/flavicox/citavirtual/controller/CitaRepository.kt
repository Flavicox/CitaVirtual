package com.flavicox.citavirtual.controller

import com.flavicox.citavirtual.model.Cita
import com.flavicox.citavirtual.model.DrProg

object CitaRepository {
    private val citasRegistradas = mutableListOf<Cita>()
    private val horariosDisponibles = mutableListOf<DrProg>()

    fun guardarCita(cita: Cita) {
        citasRegistradas.add(cita)
    }

    fun agregarHorarios(mock: List<DrProg>) {
        horariosDisponibles.clear()
        horariosDisponibles.addAll(mock)
    }

    fun eliminarHorarioReservado(drProg: DrProg) {
        horariosDisponibles.remove(drProg)
    }

}

