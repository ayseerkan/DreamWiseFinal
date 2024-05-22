package com.example.dreamwise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dreamwise.databinding.ItemDreamBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DreamAdapter(private var dreams: List<Dream>) : RecyclerView.Adapter<DreamAdapter.DreamViewHolder>() {

    inner class DreamViewHolder(val binding: ItemDreamBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamViewHolder {
        val binding = ItemDreamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DreamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DreamViewHolder, position: Int) {
        val currentDream = dreams[position]
        holder.binding.dreamTitle.text = currentDream.title
        holder.binding.dreamDescription.text = currentDream.description

        // Format the date and set it to the TextView
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = Date(currentDream.date)
        holder.binding.dreamDate.text = dateFormat.format(date)
    }

    override fun getItemCount(): Int {
        return dreams.size
    }

    fun updateDreams(newDreams: List<Dream>) {
        dreams = newDreams
        notifyDataSetChanged()
    }
}
