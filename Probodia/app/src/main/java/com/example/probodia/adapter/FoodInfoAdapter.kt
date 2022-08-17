package com.example.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.databinding.ItemFoodInfoBinding

class FoodInfoAdapter(val dataSet : List<Pair<String, Double>>) : RecyclerView.Adapter<FoodInfoAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemFoodInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Pair<String, Double>) {
            binding.ingredientNameText.text = item.first
            binding.ingredientQuantityText.text = "${item.second}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_food_info,
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