package com.piri.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.databinding.ItemChallengeParticipatingBinding
import java.time.LocalDate
import java.time.Period

class ChallengeParticipatingAdapter : RecyclerView.Adapter<ChallengeParticipatingAdapter.ViewHolder> () {

    interface OnItemClickListener {
        fun onItemClick(position : Int)
    }

    var clickListener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        clickListener = listener
    }

    private var dataSet : MutableList<ChallengeDto> = mutableListOf()

    fun setData(newDataSet : MutableList<ChallengeDto>) {
        dataSet = newDataSet
    }

    fun getData(position : Int) = dataSet[position]

    inner class ViewHolder(val binding : ItemChallengeParticipatingBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.challengeImage.clipToOutline = true

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (clickListener != null) {
                        clickListener!!.onItemClick(position)
                    }
                }
            }
        }

        fun bind(item : ChallengeDto) {
            binding.challengeTitle.text = item.name

            binding.challengeRule.text =
                "${item.frequency.dateType} ${item.frequency.period}회 ${item.frequency.times}번씩"

            binding.challengeImage.setImageResource(
                when (item.type) {
                    "혈당기록" -> R.drawable.challenge_glucose
                    "음식기록" -> R.drawable.challenge_meal
                    else -> R.drawable.challenge_image
                }
            )

            val edDate = LocalDate.parse(item.enDate)
            val stDate = LocalDate.parse(item.stDate)

            binding.challengeUntil.text = "${Period.between(stDate, edDate).days / 7}주"

            binding.challengeDate.text =
                "${stDate.monthValue}월 ${stDate.dayOfMonth}일 ~ ${edDate.monthValue}월 ${edDate.dayOfMonth}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengeParticipatingAdapter.ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_challenge_participating,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ChallengeParticipatingAdapter.ViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = dataSet.size
}