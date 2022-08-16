package com.example.probodia.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.toSpanned
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.data.remote.model.*
import com.example.probodia.databinding.ItemRecordBinding
import com.example.probodia.databinding.ItemRecordSortationBinding

class RecordTodayAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val glucoseType = 1
    val pressureType = 2
    val medicineType = 3
    val mealType = 4
    val sortationType = 5

    var dataSet : MutableList<RecordDatasBase> = mutableListOf()

    fun addDataSet(newDataSet : MutableList<RecordDatasBase>) {
        dataSet.addAll(newDataSet)
    }

    fun resetDataSet() {
        dataSet = mutableListOf()
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].type) {
            "SUGAR" -> glucoseType
            "PRESSURE" -> pressureType
            "MEDICINE" -> medicineType
            "MEAL" -> mealType
            "SORTATION" -> sortationType
            else -> 0
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            sortationType -> SortationViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record_sortation,
                parent,
                false
            ))

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
            sortationType -> {
                (holder as SortationViewHolder).bind(dataSet[position] as SortationDto)
                holder.setIsRecyclable(false)
            }

            glucoseType -> {
                (holder as GlucoseViewHolder).bind(dataSet[position] as GlucoseDto)
                holder.setIsRecyclable(false)
            }

            pressureType -> {
                (holder as PressureViewHolder).bind(dataSet[position] as PressureDto)
                holder.setIsRecyclable(false)
            }

            medicineType -> {
                (holder as MedicineViewHolder).bind(dataSet[position] as MedicineDto)
                holder.setIsRecyclable(false)
            }

            mealType -> {
                (holder as MealViewHolder).bind(dataSet[position] as MealDto)
                holder.setIsRecyclable(false)
            }
        }
    }

    override fun getItemCount() = dataSet.size

    inner class SortationViewHolder(val binding : ItemRecordSortationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SortationDto) {
            binding.sortationText.text = item.record.timeTag
            if (item.record.timeTag == "아침") {
                val layoutParams = binding.recordSortationBaseLayout.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = 0
                binding.recordSortationBaseLayout.layoutParams = layoutParams
            }
            if (item.record.itemCnt != 0) {
                binding.recordNullText.visibility = View.GONE
            }
        }
    }

    inner class GlucoseViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : GlucoseDto) {
            binding.kindText.text = item.record.timeTag.slice(IntRange(3, 4)) + "혈당"
            val str = "${item.record.glucose} mg/dL"
            addTextView(binding, getBoldText(str, str.length - 5))
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
        }
    }

    inner class PressureViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : PressureDto) {
            binding.kindText.text = "혈압"
            val str = "${item.record.maxPressure} / ${item.record.minPressure} / ${item.record.heartRate}"
            addTextView(binding, getBoldText(str, str.length))
            addTextView(binding, getBoldText("최고 / 최저 / 맥박수", 0))
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
        }
    }

    inner class MedicineViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MedicineDto) {
            binding.kindText.text = "투약"
//            binding.contentText.text = item.record.medicineName
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
//            binding.unitText.text = "Unit"
        }
    }

    inner class MealViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MealDto) {
            binding.kindText.text = "음식"
            for(i in 0 until item.record.mealDetails.size) {
                var str = "${item.record.mealDetails[i].foodName} ${item.record.mealDetails[i].quantity} g"
                addTextView(binding, getBoldText(str, str.length - 1))
            }
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
        }
    }

    fun getBoldText(str : String, length : Int) : SpannableStringBuilder {
        var boldStr = SpannableStringBuilder(str)
        boldStr.setSpan(
            StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return boldStr
    }

    fun addTextView(binding : ItemRecordBinding, text : SpannableStringBuilder) {
        val contentTextView = TextView(binding.root.context)
        contentTextView.text = text
        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        param.topMargin = 10
        contentTextView.layoutParams = param
        binding.contentLayout.addView(contentTextView)
    }

    fun getDisplayTime(time : String): String {
        val hour = time.slice(IntRange(0, 1)).toInt()
        return if (hour < 12) {
            "오전 ${hour}"
        } else {
            "오후 ${hour - 12}"
        } + ":${time.slice(IntRange(3, 4))}"
    }
}