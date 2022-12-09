package com.piri.probodia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.ShowDoctorDto
import com.piri.probodia.databinding.ItemShowDoctorBinding

class ShowDoctorListAdapter(val setItemHeight : (height : Int) -> Unit) : RecyclerView.Adapter<ShowDoctorListAdapter.ViewHolder>() {

    val dataSet : MutableList<ShowDoctorDto> = mutableListOf()

    private var isItemHeight = false

    fun addDataSet(newDataSet : MutableList<ShowDoctorDto>) {
        dataSet.addAll(findPosition(newDataSet[0]), newDataSet)
    }

    private fun findPosition(showDoctorDto: ShowDoctorDto) : Int {
        for(i in dataSet.size - 1 downTo 0) {
            if (showDoctorDto.date <= dataSet[i].date) {
                return i + 1
            }
        }
        return 0
    }

    fun getLastItem() : ShowDoctorDto? {
        return if (dataSet.size > 0) dataSet.last() else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_show_doctor,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(val binding : ItemShowDoctorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : ShowDoctorDto) {
            binding.dateText.text = item.date

            binding.text1.text = item.values[0].toString().let { if (it != "-1") it else "-"}
            binding.text2.text = item.values[1].toString().let { if (it != "-1") it else "-"}
            binding.text3.text = item.values[2].toString().let { if (it != "-1") it else "-"}
            binding.text4.text = item.values[3].toString().let { if (it != "-1") it else "-"}
            binding.text5.text = item.values[4].toString().let { if (it != "-1") it else "-"}
            binding.text6.text = item.values[5].toString().let { if (it != "-1") it else "-"}

            getItemHeight()
        }

        private fun getItemHeight() {
            if (isItemHeight == false) {
                isItemHeight = true
                if (binding.root.height != 0) {
                    setItemHeight(binding.root.height)
                } else {
                    binding.root.viewTreeObserver.addOnGlobalLayoutListener(
                        object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)

                                setItemHeight(binding.root.height)
                            }
                        }
                    )
                }
            }
        }
    }
}