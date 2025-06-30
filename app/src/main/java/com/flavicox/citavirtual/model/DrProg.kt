package com.flavicox.citavirtual.model

import java.io.Serializable
import java.util.Date

data class DrProg(
    val id: Int,
    val doctor: Doctor,
    val fecha: Date,
    val hora: String
) : Serializable