package com.piri.probodia.view.fragment.record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.piri.probodia.R
import com.piri.probodia.databinding.FragmentDatePickerDialogBinding
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class DatePickerDialogFragment(val setDate : (year : Int, month : Int, date : Int) -> Unit, val localDateTime : LocalDateTime) : DialogFragment() {

    private lateinit var binding : FragmentDatePickerDialogBinding

    private var year = 0
    private var month = 0
    private var day = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_picker_dialog, container, false)

        year = localDateTime.year
        month = localDateTime.monthValue
        day = localDateTime.dayOfMonth

        dialog?.window?.setBackgroundDrawableResource(R.drawable.white_pure_2_background)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, localDateTime.year)
        calendar.set(Calendar.MONTH, localDateTime.monthValue - 1)
        calendar.set(Calendar.DAY_OF_MONTH, localDateTime.dayOfMonth)

        binding.datePicker.setDate(calendar.timeInMillis, true, true)

        binding.datePicker.setOnDateChangeListener { view, _year, _month, _dayOfMonth ->
            year = _year
            month = _month + 1
            day = _dayOfMonth
        }

        binding.enterBtn.setOnClickListener {
            val todayDateTime = LocalDateTime.now()
            var testDateTime = todayDateTime.withYear(year)
            testDateTime = testDateTime.withMonth(month)
            testDateTime = testDateTime.withDayOfMonth(day)
            if (todayDateTime >= testDateTime) {
                setDate(year, month, day)
                parentFragmentManager.beginTransaction().remove(this).commit()
            } else {
                Toast.makeText(requireContext(), "기록 시간이 현재 시간보다 빠를 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}