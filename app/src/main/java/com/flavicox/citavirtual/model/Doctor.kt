package com.flavicox.citavirtual.model

import java.io.Serializable

data class Doctor(
    val id: Int,
    val nombre: String,
    val apPaterno: String,
    val apMaterno: String,
    val especialidad: String,
    val telefono: String
) : Serializable
