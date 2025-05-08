package com.flavicox.citavirtual.model

import java.util.Date

data class Cita(
    val fecha: Date,
    val sintomas: String,
    val diagnostico: String,
    val alergias: String,
    val aceptaTerm: Boolean,
    val doctor: Doctor,
    val paciente: Paciente
)
