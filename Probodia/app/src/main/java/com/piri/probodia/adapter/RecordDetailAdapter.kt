package com.piri.probodia.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ItemRecordDetailDataDto
import com.piri.probodia.databinding.ItemRecordDetailDataBinding
import com.piri.probodia.databinding.ItemRecordDetailSortationBinding

class RecordDetailAdapter(
    val sortation : String,
    val dataSet : List<ItemRecordDetailDataDto>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val sortationType = 0
    private val dataType = 1

    override fun getItemViewType(position: Int): Int {
        if (sortation == "" || position != 0) {
            return dataType
        } else {
            return sortationType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            sortationType -> SortationViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record_detail_sortation,
                parent,
                false
            ))

            dataType -> DataViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record_detail_data,
                parent,
                false
            ))

            else -> SortationViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_record_detail_sortation,
                parent,
                false
            ))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            sortationType -> {
                (holder as SortationViewHolder).bind(sortation)
                holder.setIsRecyclable(false)
            }

            dataType -> {
                if (sortation == "") {
                    (holder as DataViewHolder).bind(dataSet[position])
                } else {
                    (holder as DataViewHolder).bind(dataSet[position - 1])
                }
                holder.setIsRecyclable(false)
            }
        }
    }

    override fun getItemCount() : Int {
        if (sortation == "") {
            return dataSet.size
        } else {
            return dataSet.size + 1
        }
    }

    inner class SortationViewHolder(val binding : ItemRecordDetailSortationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name : String) {
            binding.recordSortationText.text = name
        }
    }

    inner class DataViewHolder(val binding : ItemRecordDetailDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data : ItemRecordDetailDataDto) {
            binding.recordDataName.text = data.baseDataName
            binding.recordDataUnit.text = data.baseDataUnit
            binding.recordDataText.text = data.baseDataText.toString()
        }
    }
}