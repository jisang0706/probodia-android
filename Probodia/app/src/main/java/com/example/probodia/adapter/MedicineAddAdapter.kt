package com.example.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.databinding.ItemMedicineBinding

class MedicineAddAdapter : RecyclerView.Adapter<MedicineAddAdapter.ViewHolder>() {

//    private dataSet : MutableList<MedicineItem>

    inner class ViewHolder(val binding : ItemMedicineBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_medicine,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(dataSet[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = 0
}