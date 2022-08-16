package com.example.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.data.remote.model.ApiFoodDto
import com.example.probodia.databinding.ItemFoodBinding

class FoodSearchAdapter() : RecyclerView.Adapter<FoodSearchAdapter.ViewHolder>() {

    var dataSet : MutableList<ApiFoodDto.Body.FoodItem> = mutableListOf()

    fun addDataSet(newDataSet : MutableList<ApiFoodDto.Body.FoodItem>) {
        dataSet.addAll(newDataSet)
    }

    fun resetDataSet() {
        dataSet = mutableListOf()
    }

    inner class ViewHolder(val binding : ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : ApiFoodDto.Body.FoodItem) {
            binding.foodText.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_food,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = dataSet.size
}