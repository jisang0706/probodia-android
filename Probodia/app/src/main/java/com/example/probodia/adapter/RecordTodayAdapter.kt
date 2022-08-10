package com.example.probodia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.data.remote.model.*
import com.example.probodia.databinding.ItemRecordBinding

class RecordTodayAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val glucoseType = 1
    val pressureType = 2
    val medicineType = 3
    val mealType = 4

    var dataSet : TodayRecord = TodayRecord(mutableListOf())

    fun addDataSet(newDataSet : TodayRecord) {
        dataSet.temp.addAll(newDataSet.temp)
    }

    fun resetDataSet() {
        dataSet = TodayRecord(mutableListOf())
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet.getDatas()[position].type) {
            "SUGAR" -> glucoseType
            "PRESSURE" -> pressureType
            "MEDICINE" -> medicineType
            "MEAL" -> mealType
            else -> 0
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            glucoseType -> GlucoseViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            pressureType -> PressureViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            medicineType -> MedicineViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            mealType -> MealViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            else -> GlucoseViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            glucoseType -> {
                (holder as GlucoseViewHolder).bind(dataSet.getDatas()[position] as GlucoseDto)
                holder.setIsRecyclable(false)
            }

            pressureType -> {
                (holder as PressureViewHolder).bind(dataSet.getDatas()[position] as PressureDto)
                holder.setIsRecyclable(false)
            }

            medicineType -> {
                (holder as MedicineViewHolder).bind(dataSet.getDatas()[position] as MedicineDto)
                holder.setIsRecyclable(false)
            }

            mealType -> {
                (holder as MealViewHolder).bind(dataSet.getDatas()[position] as MealDto)
                holder.setIsRecyclable(false)
            }
        }
    }

    override fun getItemCount() = dataSet.getDatas().size

    inner class GlucoseViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : GlucoseDto) {
            binding.kindText.text = "혈당"
            binding.contentText.text = item.record.glucose.toString()
            binding.timeText.text = item.record.recordDate
            binding.unitText.text = "mg/dL"
        }
    }

    inner class PressureViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : PressureDto) {
            binding.kindText.text = "혈압"
            binding.contentText.text = "${item.record.maxPressure} / ${item.record.minPressure} / ${item.record.heartRate}"
            binding.timeText.text = item.record.recordDate
            binding.unitText.text = "최고/최저/맥박수"
        }
    }

    inner class MedicineViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MedicineDto) {
            binding.kindText.text = "투약"
            binding.contentText.text = item.record.medicineName
            binding.timeText.text = item.record.recordDate
            binding.unitText.text = "Unit"
        }
    }

    inner class MealViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MealDto) {
            binding.kindText.text = "음식"
            binding.contentText.text = item.record.mealDetails[0].foodName
            binding.timeText.text = item.record.recordDate
            binding.unitText.text = "인분"
        }
    }
}