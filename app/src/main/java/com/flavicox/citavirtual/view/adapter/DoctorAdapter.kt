package com.flavicox.citavirtual.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flavicox.citavirtual.databinding.ItemDoctorBinding
import com.flavicox.citavirtual.model.DrProg
import com.flavicox.citavirtual.view.CitaActivity
import java.text.SimpleDateFormat
import java.util.Locale

class DoctorAdapter (
    private var listaDoctores: List<DrProg>,
    private val context: Context, // nuevo
    private val onItemClick: (DrProg) -> Unit
): RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>()  {
    inner class DoctorViewHolder(val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDoctorBinding.inflate(inflater, parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val drProg = listaDoctores[position]
        val doctor = drProg.doctor

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.binding.tvFecha.text = sdf.format(drProg.fecha)

        holder.binding.tvNombreDoctor.text = "Dr. ${doctor.nombre} ${doctor.apPaterno} ${doctor.apMaterno}"
        holder.binding.tvEspecialidad.text = doctor.especialidad
        holder.binding.tvHora.text = drProg.hora

        holder.binding.root.setOnClickListener {
            onItemClick(drProg) // por si lo quieres mantener
            val intent = Intent(context, CitaActivity::class.java).apply {
                putExtra("doctor", drProg.doctor)
                putExtra("fecha", drProg.fecha.time) // Date como long
                putExtra("hora", drProg.hora)
            }
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = listaDoctores.size

    fun actualizarLista(nuevaLista: List<DrProg>) {
        listaDoctores = nuevaLista
        notifyDataSetChanged()
    }

}