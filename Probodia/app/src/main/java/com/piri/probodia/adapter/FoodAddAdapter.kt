package com.piri.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.databinding.ItemFoodAddBinding

class FoodAddAdapter : RecyclerView.Adapter<FoodAddAdapter.ViewHolder>() {

    interface OnItemButtonClickListener {
        fun onItemDeleteClick(position : Int)

        fun onItemEditClick(position : Int)
    }

    var clickListener : OnItemButtonClickListener? = null

    fun setOnItemButtonClickListener(listener : OnItemButtonClickListener) {
        clickListener = listener
    }

    private val dataSet : MutableList<PostMealBody.PostMealItem> = mutableListOf()

    fun addItem(item : PostMealBody.PostMealItem) {
        dataSet.add(0, item)
    }

    fun deleteItem(position : Int) {
        dataSet.removeAt(position)
    }

    fun getItem(position : Int) = dataSet[position]

    fun getList() = dataSet

    inner class ViewHolder(val binding : ItemFoodAddBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (clickListener != null) {
                        clickListener!!.onItemDeleteClick(position)
                    }
                }
            }

            binding.editBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (clickListener != null) {
                        clickListener!!.onItemEditClick(position)
                    }
                }
            }
        }

        fun bind(item : PostMealBody.PostMealItem) {
            binding.foodNameText.text = "${item.foodName} ${item.quantity}g"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAddAdapter.ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_food_add,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FoodAddAdapter.ViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = dataSet.size
}