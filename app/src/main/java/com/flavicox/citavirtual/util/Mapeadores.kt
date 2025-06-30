package com.flavicox.citavirtual.util

import com.flavicox.citavirtual.bd.entity.CitaEntidad
import com.flavicox.citavirtual.bd.entity.DoctorEntidad
import com.flavicox.citavirtual.bd.entity.ProgramacionDoctorEntidad
import com.flavicox.citavirtual.model.Cita
import com.flavicox.citavirtual.model.Doctor
import com.flavicox.citavirtual.model.DrProg
import java.util.Date

fun DoctorEntidad.aModelo(): Doctor = Doctor(
    id = id,
    nombre = nombre,
    apPaterno = apPaterno,
    apMaterno = apMaterno,
    especialidad = especialidad,
    telefono = telefono
)

fun Doctor.aEntidad(): DoctorEntidad = DoctorEntidad(
    id = id,
    nombre = nombre,
    apPaterno = apPaterno,
    apMaterno = apMaterno,
    especialidad = especialidad,
    telefono = telefono
)

fun ProgramacionDoctorEntidad.aModelo(doctor: Doctor): DrProg = DrProg(
    id = id,
    doctor = doctor,
    fecha = Date(fecha),  // ← convertimos de Long a Date
    hora = hora
)

fun DrProg.aEntidad(): ProgramacionDoctorEntidad = ProgramacionDoctorEntidad(
    id = id,
    doctorId = doctor.id,
    fecha = fecha.time,  // ← convertimos de Date a Long
    hora = hora
)

fun Cita.aEntidad(): CitaEntidad = CitaEntidad(
    id = id,
    doctorId = doctor.id,
    fecha = fecha.time,
    sintomas = sintomas,
    diagnostico = diagnostico,
    alergias = alergias,
    aceptaTerm = aceptaTerm
)