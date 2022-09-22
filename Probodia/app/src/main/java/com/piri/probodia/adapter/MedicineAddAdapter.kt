package com.piri.probodia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ApiMedicineDto
import com.piri.probodia.databinding.ItemMedicineBinding
import com.piri.probodia.databinding.ItemMedicinePlusBinding

class MedicineAddAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSet : MutableList<ApiMedicineDto.Body.MedicineItem> = mutableListOf()

    interface OnItemButtonClickListener {
        fun onItemDeleteClick(position : Int)
        fun onItemSearchClick(position : Int)

        fun onItemPlusClick()
    }

    var clickListener : OnItemButtonClickListener? = null

    fun setOnItemButtonClickListener(listener : OnItemButtonClickListener) {
        clickListener = listener
    }

    fun getList() = dataSet

    fun addItem(item : ApiMedicineDto.Body.MedicineItem) {
        dataSet.add(item)
        setItemUnit(dataSet.size - 1, 1)
    }

    fun deleteItem(position : Int) {
        dataSet.removeAt(position)
    }

    fun setItem(position : Int, item : ApiMedicineDto.Body.MedicineItem) {
        val unit = dataSet[position].unit
        dataSet[position] = item
        setItemUnit(position, unit)
    }

    fun setItemUnit(position : Int, unit : Int) {
        dataSet[position].unit = unit
    }

    fun checkItemComplete() : Boolean {
        if (dataSet.size == 0)  return false
        for(data in dataSet) {
            if (data.item_seq == "" || data.unit == 0) {
                return false
            }
        }
        return true
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= dataSet.size) {
            return 1
        } else {
            return 0
        }
    }

    override fun onCreateViewHolder(
        parent : ViewGroup,
        viewType: Int
    ) : RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> MedicineViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_medicine,
                parent,
                false
            ))
            else -> MedicinePlusViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_medicine_plus,
                parent,
                false
            ))
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(getItemViewType(position)) {
            0 -> {
                (holder as MedicineViewHolder).bind(dataSet[position])
                holder.setIsRecyclable(false)
            }

            1 -> {
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class MedicineViewHolder(val binding : ItemMedicineBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteMedicineLayout.setOnClickListener {
                binding.deleteMedicineBtn.callOnClick()
            }

            binding.deleteMedicineBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (clickListener != null) {
                        clickListener!!.onItemDeleteClick(position)
                    }
                }
            }

            binding.medicineBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (clickListener != null) {
                        clickListener!!.onItemSearchClick(position)
                    }
                }
            }

            binding.unitEdit.addTextChangedListener {
                val position = bindingAdapterPosition
                if (it.toString() != "") {
                    dataSet[position].unit = it.toString().toInt()
                    Log.e("MEDICINECOMPLETE", dataSet[position].unit.toString())
                } else {
                    dataSet[position].unit = 0
                }
            }
        }

        fun bind(item : ApiMedicineDto.Body.MedicineItem) {
            binding.medicineBtn.text = item.itemName
            if (item.unit != 0) {
                binding.unitEdit.setText("${item.unit}")
            }
        }
    }

    inner class MedicinePlusViewHolder(val binding : ItemMedicinePlusBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.medicinePlusBtn.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (clickListener != null) {
                        clickListener!!.onItemPlusClick()
                    }
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size + 1
}