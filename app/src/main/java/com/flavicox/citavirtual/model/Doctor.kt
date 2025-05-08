package com.flavicox.citavirtual.model

import java.io.Serializable

data class Doctor(
    val nombre: String,
    val apPaterno: String,
    val apMaterno: String,
    val especialidad: String
) : Serializable
