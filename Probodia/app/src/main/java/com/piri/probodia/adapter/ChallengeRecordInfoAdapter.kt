package com.piri.probodia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ChallengeRecordDto
import com.piri.probodia.databinding.ItemChallengeRecordInfoBinding

class ChallengeRecordInfoAdapter : RecyclerView.Adapter<ChallengeRecordInfoAdapter.ViewHolder> () {

    private var dataSet : MutableList<ChallengeRecordDto> = mutableListOf()

    fun setData(newDataSet : MutableList<ChallengeRecordDto>) {
        dataSet = newDataSet
    }

    inner class ViewHolder(val binding : ItemChallengeRecordInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            if (binding.root.width != 0) {
                setHeight(binding.root.width)
            } else {
                binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                            setHeight(binding.root.width)
                        }
                    }
                )
            }
        }

        fun setHeight(height : Int) {
            val params = binding.root.layoutParams
            params.height = height
            binding.root.layoutParams = params
        }

        fun bind(item : ChallengeRecordDto) {
            val contents = item.content.split(',')

            for(content in contents) {
                if (content.contains("timeTag")) {
                    binding.timetagText.text = content.split('=')[1]
                }

                if (content.contains("recordDate")) {
                    binding.dateText.text = content.split('=')[1].split(' ')[0]
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengeRecordInfoAdapter.ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_challenge_record_info,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ChallengeRecordInfoAdapter.ViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = dataSet.size
}