package com.piri.probodia.adapter

import android.animation.ObjectAnimator
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
import androidx.core.animation.addListener
import androidx.core.animation.addPauseListener
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.*
import com.piri.probodia.databinding.ItemRecordBinding
import com.piri.probodia.databinding.ItemRecordEmptyBinding
import com.piri.probodia.databinding.ItemRecordSortationBinding
import com.piri.probodia.widget.utils.Convert

class RecordTodayAdapter(val past : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataSet : MutableList<RecordDatasBase> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(data : RecordDatasBase)
        fun onRecordClick(data : SortationDto, kind : Int)
    }

    var clickListener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        clickListener = listener
    }

    fun addDataSet(newDataSet : MutableList<RecordDatasBase>) {
        dataSet.addAll(findPosition(newDataSet[0] as SortationDto), newDataSet)
    }

    fun findPosition(sortation : SortationDto) : Int {
        val sortationPosition = buildList {
            for(i in 0 until dataSet.size) {
                if (dataSet[i].type == "SORTATION") {
                    add(i)
                }
            }
        }
        for(i in sortationPosition.size - 1 downTo 0) {
            val truePosition : () -> Int = {
                if (i == sortationPosition.size - 1) {
                    dataSet.size
                } else {
                    sortationPosition[i + 1]
                }
            }
            if ((dataSet[sortationPosition[i]] as SortationDto).record.recordDate > sortation.record.recordDate) {
                return truePosition()
            }
            if ((dataSet[sortationPosition[i]] as SortationDto).record.recordDate == sortation.record.recordDate) {
                val timeTag = (dataSet[sortationPosition[i]] as SortationDto).record.timeTag
                when (sortation.record.timeTag) {
                    "저녁" -> if (timeTag == "점심" || timeTag == "아침") {
                        return truePosition()
                    }

                    "점심" -> if (timeTag == "아침") {
                        return truePosition()
                    }
                }
            }
        }
        return 0
    }

    fun resetDataSet() {
        dataSet = mutableListOf()
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSet[position].type) {
            "SUGAR" -> R.integer.glucose_type
            "PRESSURE" -> R.integer.pressure_type
            "MEDICINE" -> R.integer.medicine_type
            "MEAL" -> R.integer.meal_type
            "SORTATION" -> R.integer.sortation_type
            "EMPTY" -> R.integer.empty_type
            else -> 0
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            R.integer.sortation_type -> SortationViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record_sortation,
                parent,
                false
            ))

            R.integer.empty_type -> EmptyViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record_empty,
                parent,
                false
            ))

            R.integer.glucose_type -> GlucoseViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            R.integer.pressure_type -> PressureViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            R.integer.medicine_type -> MedicineViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record,
                parent,
                false
            ))

            R.integer.meal_type -> MealViewHolder(DataBindingUtil.inflate(
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
            R.integer.sortation_type -> {
                (holder as SortationViewHolder).bind(dataSet[position] as SortationDto)
                holder.setIsRecyclable(false)
            }

            R.integer.empty_type -> {
                (holder as EmptyViewHolder).bind(dataSet[position] as RecordEmptyDto)
                holder.setIsRecyclable(false)
            }

            R.integer.glucose_type -> {
                (holder as GlucoseViewHolder).bind(dataSet[position] as GlucoseDto)
                holder.setIsRecyclable(false)
            }

            R.integer.pressure_type -> {
                (holder as PressureViewHolder).bind(dataSet[position] as PressureDto)
                holder.setIsRecyclable(false)
            }

            R.integer.medicine_type -> {
                (holder as MedicineViewHolder).bind(dataSet[position] as MedicineDto)
                holder.setIsRecyclable(false)
            }

            R.integer.meal_type -> {
                (holder as MealViewHolder).bind(dataSet[position] as MealDto)
                holder.setIsRecyclable(false)
            }
        }
    }

    override fun getItemCount() = dataSet.size

    inner class SortationViewHolder(val binding : ItemRecordSortationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : SortationDto) {
            val position = bindingAdapterPosition
            binding.sortationText.text = item.record.timeTag
            if (item.record.timeTag == "아침" && past) {
                val layoutParams = binding.recordSortationBaseLayout.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = 0
                binding.recordSortationBaseLayout.layoutParams = layoutParams
                binding.datetimeText.text = item.record.recordDate
            } else {
                binding.datetimeText.visibility = View.GONE
            }
            if (past && position == dataSet.size - 1) {
                binding.loadingProgress.visibility = View.VISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
            }

            binding.recordGlucoseBtn.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onRecordClick(item, R.integer.record_glucose)
                }
            }

            binding.recordPressureBtn.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onRecordClick(item, R.integer.record_pressure)
                }
            }

            binding.recordMedicineBtn.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onRecordClick(item, R.integer.record_medicine)
                }
            }

            binding.recordMealBtn.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onRecordClick(item, R.integer.record_meal)
                }
            }

            var isRecordBtnShow = false
            binding.recordSortationBaseLayout.setOnClickListener {
                if (isRecordBtnShow) {
                    binding.recordBtnBaseLayout.animate().withEndAction {
                        val params = binding.baseLayout.layoutParams
                        params.height =
                            binding.baseLayout.height - binding.recordBtnBaseLayout.height - 30
                        binding.baseLayout.layoutParams = params
                        binding.recordBtnBaseLayout.visibility = View.GONE
                    }.start()
                    val moveRecordBtn = ObjectAnimator
                        .ofFloat(
                            binding.recordBtnBaseLayout,
                            "y",
                            binding.recordSortationBaseLayout.bottom.toFloat() + 15f,
                            binding.recordSortationBaseLayout.bottom.toFloat() - (binding.recordSortationBaseLayout.height / 2)
                        )
                        .setDuration(300)

                    moveRecordBtn.start()
                    isRecordBtnShow = false
                } else {
                    binding.recordBtnBaseLayout.visibility = View.INVISIBLE
                    val params = binding.baseLayout.layoutParams
                    params.height =
                        binding.baseLayout.height + binding.recordBtnBaseLayout.height + 30
                    binding.baseLayout.layoutParams = params
                    binding.recordBtnBaseLayout.visibility = View.VISIBLE

                    val moveRecordBtn = ObjectAnimator
                        .ofFloat(
                            binding.recordBtnBaseLayout,
                            "y",
                            binding.recordSortationBaseLayout.bottom.toFloat() - (binding.recordSortationBaseLayout.height / 2),
                                    binding.recordSortationBaseLayout.bottom.toFloat() + 15f
                        )
                        .setDuration(300)

                    moveRecordBtn.start()
                    isRecordBtnShow = true
                }
            }
        }
    }

    inner class EmptyViewHolder(val binding : ItemRecordEmptyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : RecordEmptyDto) {
            val position = bindingAdapterPosition
            if (past && position == dataSet.size - 1) {
                binding.loadingProgress.visibility = View.VISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
            }
        }
    }

    inner class GlucoseViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : GlucoseDto) {
            val position = bindingAdapterPosition
            binding.kindText.text = item.record.timeTag.slice(IntRange(3, 4)) + " 혈당"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange_800))
            binding.kindText.setBackgroundResource(R.drawable.orange_1_background)
            val str = "${item.record.glucose} mg/dL"
            addTextView(binding, getBoldText(str, str.length - 5))
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])

            binding.root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onItemClick(dataSet[position])
                }
            }

            if (past && position == dataSet.size - 1) {
                binding.loadingProgress.visibility = View.VISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
            }
        }
    }

    inner class PressureViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : PressureDto) {
            val position = bindingAdapterPosition
            binding.kindText.text = "혈압"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red_800))
            binding.kindText.setBackgroundResource(R.drawable.red_1_background)
            val str = "${item.record.maxPressure} / ${item.record.minPressure} / ${item.record.heartRate}"
            addTextView(binding, getBoldText(str, str.length))
            addTextView(binding, getBoldText("최고 / 최저 / 맥박수", 0))
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])

            binding.root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onItemClick(dataSet[position])
                }
            }

            if (past && position == dataSet.size - 1) {
                binding.loadingProgress.visibility = View.VISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
            }
        }
    }

    inner class MedicineViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MedicineDto) {
            val position = bindingAdapterPosition
            binding.kindText.text = "투약"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue_800))
            binding.kindText.setBackgroundResource(R.drawable.blue_1_background)
            val medicineName = fun(medicineName : String): String { if (medicineName.length > 5) return "${medicineName.substring(0, 5)}.." else return medicineName }
            for(i in item.record.medicineDetails.indices) {
                val str = "${medicineName(item.record.medicineDetails[i].medicineName)} ${item.record.medicineDetails[i].medicineCnt} Unit"
                addTextView(binding, getBoldText(str, str.length - 4))
            }
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])

            binding.root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onItemClick(dataSet[position])
                }
            }

            if (past && position == dataSet.size - 1) {
                binding.loadingProgress.visibility = View.VISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
            }
        }
    }

    inner class MealViewHolder(val binding : ItemRecordBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MealDto) {
            val position = bindingAdapterPosition
            binding.kindText.text = "음식"
            binding.kindText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_800))
            binding.kindText.setBackgroundResource(R.drawable.green_1_background)
            for(i in item.record.mealDetails.indices) {
                var str = "${item.record.mealDetails[i].foodName} ${item.record.mealDetails[i].quantity} g"
                addTextView(binding, getBoldText(str, str.length - 1))
            }
            binding.timeText.text = getDisplayTime(item.record.recordDate.split(" ")[1])

            binding.root.setOnClickListener {
                if (position != RecyclerView.NO_POSITION && clickListener != null) {
                    clickListener!!.onItemClick(dataSet[position])
                }
            }

            if (past && position == dataSet.size - 1) {
                binding.loadingProgress.visibility = View.VISIBLE
            } else {
                binding.loadingProgress.visibility = View.GONE
            }
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
            "오전 $hour"
        } else {
            "오후 ${hour - 12}"
        } + ":${time.slice(IntRange(3, 4))}"
    }
}