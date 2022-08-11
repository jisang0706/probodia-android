package com.example.probodia.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.databinding.FragmentTimeSelectorBinding
import com.example.probodia.viewmodel.RecordAnythingViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimeSelectorFragment : Fragment() {

    private lateinit var binding : FragmentTimeSelectorBinding
    private lateinit var viewModel : RecordAnythingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_selector, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(RecordAnythingViewModel::class.java)
        binding.vm = viewModel

        viewModel.localDateTime = LocalDateTime.now()
        binding.datePickerBtn.text = viewModel.localDateTime.format(DateTimeFormatter.ofPattern("yy년 MM월 dd일"))
        binding.timePickerBtn.text = viewModel.localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        if (viewModel.num != 1) {
            binding.secLayout.visibility = View.GONE
            binding.morningFirBtn.text = "아침"
            binding.lunchFirBtn.text = "점심"
            binding.dinnerFirBtn.text = "저녁"
        }

        binding.morningFirBtn.setBackgroundResource(R.drawable.orange_100_1_background)
        binding.morningFirBtn.setTextColor(Color.WHITE)

        viewModel.selectedTimeTag.observe(requireActivity(), Observer {
            val buttons = mutableListOf(
                binding.morningFirBtn, binding.lunchFirBtn, binding.dinnerFirBtn,
                binding.morningSecBtn, binding.lunchSecBtn, binding.dinnerSecBtn)

            for(i in 0 until buttons.size) {
                if (it == i + 1) {
                    buttons[i].setBackgroundResource(R.drawable.orange_100_1_background)
                    buttons[i].setTextColor(Color.WHITE)
                } else {
                    buttons[i].setBackgroundResource(R.drawable.white_1_background)
                    buttons[i].setTextColor(Color.BLACK)
                }
            }
        })

        return binding.root
    }
}