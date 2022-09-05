package com.example.probodia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.data.remote.model.ApiMedicineDto
import com.example.probodia.databinding.ItemMedicineBinding
import com.example.probodia.databinding.ItemMedicinePlusBinding

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
    }

    fun deleteItem(position : Int) {
        dataSet.removeAt(position)
    }

    fun setItem(position : Int, item : ApiMedicineDto.Body.MedicineItem) {
        dataSet[position] = item
    }

    fun setItemUnit(position : Int, unit : Int) {
        dataSet[position].unit = unit
    }

    fun checkItemComplete() : Boolean {
        Log.e("MEDICINECOMPLETE", dataSet.size.toString())
        if (dataSet.size == 0)  return false
        for(data in dataSet) {
            Log.e("MEDICINECOMPLETE", "${data.item_seq} ${data.unit}")
            if (data.item_seq == "" || data.unit == 0) {
                return false
            }
        }
        Log.e("MEDICINECOMPLETE", "COMPLETE")
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
                if (binding.unitEdit.text.toString() != "") {
                    dataSet[position].unit = binding.unitEdit.text.toString().toInt()
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