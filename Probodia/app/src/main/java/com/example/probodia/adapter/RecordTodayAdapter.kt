package com.example.probodia.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.toSpanned
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.data.remote.model.*
import com.example.probodia.databinding.ItemRecordBinding
import com.example.probodia.databinding.ItemRecordSortationBinding
import com.example.probodia.widget.utils.Convert
import kotlin.coroutines.coroutineContext

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
            binding.kindText.text = item.record.timeTag.slice(IntRange(3, 4)) + " 혈당"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange_800))
            binding.kindText.setBackgroundResource(R.drawable.orange_1_background)
            val str = "${item.record.glucose} mg/dL"
            addTextView(binding, getBoldText(str, str.length - 5))
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
        }
    }

    inner class PressureViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : PressureDto) {
            binding.kindText.text = "혈압"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red_800))
            binding.kindText.setBackgroundResource(R.drawable.red_1_background)
            val str = "${item.record.maxPressure} / ${item.record.minPressure} / ${item.record.heartRate}"
            addTextView(binding, getBoldText(str, str.length))
            addTextView(binding, getBoldText("최고 / 최저 / 맥박수", 0))
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
        }
    }

    inner class MedicineViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MedicineDto) {
            binding.kindText.text = "투약"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue_800))
            binding.kindText.setBackgroundResource(R.drawable.blue_1_background)
//            binding.contentText.text = item.record.medicineName
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])
//            binding.unitText.text = "Unit"
        }
    }

    inner class MealViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MealDto) {
            binding.kindText.text = "음식"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_800))
            binding.kindText.setBackgroundResource(R.drawable.green_1_background)
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
        boldStr.setSpan(ForegroundColorSpan(Color.BLACK), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        boldStr.setSpan(RelativeSizeSpan(0.7f), length, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return boldStr
    }

    fun addTextView(binding : ItemRecordBinding, text : SpannableStringBuilder) {
        val contentTextView = TextView(binding.root.context)
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15F)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, Convert.floatToDp(6F), 0, Convert.floatToDp(6F))
        contentTextView.layoutParams = params
        contentTextView.text = text
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