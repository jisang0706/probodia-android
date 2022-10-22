package com.piri.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.databinding.ItemFoodInfoBinding

class FoodInfoAdapter(private val dataSet : MutableList<Pair<String, Double>>) : RecyclerView.Adapter<FoodInfoAdapter.ViewHolder>() {

    init {
        dataSet.removeIf { it.second < 0 }
    }

    inner class ViewHolder(val binding : ItemFoodInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Pair<String, Double>) {
            binding.ingredientNameText.text = item.first
            val unit = { name : String -> if(name == "나트륨") "mg" else "g"}
            binding.ingredientQuantityText.text = "${String.format("%.1f", item.second)}${unit(item.first)}"
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