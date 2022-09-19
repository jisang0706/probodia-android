package com.example.probodia.view.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.probodia.R
import com.example.probodia.databinding.FragmentTimePickerDialogBinding

class TimePickerDialogFragment(val setTime : (hour : Int, minute : Int) -> Unit, val hour : Int, val minute : Int) : DialogFragment() {

    private lateinit var binding : FragmentTimePickerDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_picker_dialog, container, false)

        dialog?.window?.setBackgroundDrawableResource(R.drawable.white_pure_2_background)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = 23
        binding.hourPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.hourPicker.value = hour

        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
        binding.minutePicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.minutePicker.value = minute

        binding.enterBtn.setOnClickListener {
            setTime(
                binding.hourPicker.value,
                binding.minutePicker.value
            )
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}