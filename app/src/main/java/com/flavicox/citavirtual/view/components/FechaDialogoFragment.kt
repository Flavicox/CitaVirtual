package com.flavicox.citavirtual.view.components

import android.app.Dialog
import android.os.Bundle
import androidx.compose.material3.DatePickerDialog
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Date

class FechaDialogoFragment(
    private val onFechaSeleccionada: (fecha: Date) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendario = Calendar.getInstance()

        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        return android.app.DatePickerDialog(requireContext(), { _, y, m, d ->
            val seleccionada = Calendar.getInstance().apply {
                set(Calendar.YEAR, y)
                set(Calendar.MONTH, m)
                set(Calendar.DAY_OF_MONTH, d)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            onFechaSeleccionada(seleccionada)
        }, año, mes, dia)
    }
}
