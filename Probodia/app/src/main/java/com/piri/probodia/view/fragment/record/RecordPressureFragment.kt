package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.PressureDto
import com.piri.probodia.databinding.FragmentRecordPressureBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordAnythingViewModel
import com.piri.probodia.viewmodel.RecordPressureViewModel
import com.piri.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordPressureFragment(val reload : () -> Unit, val recordType : Int, val data : PressureDto.Record?) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecordPressureBinding

    private lateinit var pressureViewModel : RecordPressureViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTimeSelector()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_pressure, container, false)

        pressureViewModel = ViewModelProvider(this).get(RecordPressureViewModel::class.java)
        binding.pressureVm = pressureViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(2)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        setRecordBaseTime()

        if (recordType == 1) {
            binding.highPressureEdit.setText(data!!.maxPressure.toString())
            binding.lowPressureEdit.setText(data!!.minPressure.toString())
            binding.heartRateEdit.setText(data!!.heartRate.toString())
            examineEditTextFull()
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                baseViewModel.setServerFinish(false)
                if (recordType == 1) {
                    pressureViewModel.putPressure(
                        PreferenceRepository(requireContext()),
                        data!!.recordId,
                        getSelectedTimeTag(),
                        binding.highPressureEdit.text.toString().toInt(),
                        binding.lowPressureEdit.text.toString().toInt(),
                        binding.heartRateEdit.text.toString().toInt(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                } else {
                    pressureViewModel.postPressure(
                        PreferenceRepository(requireContext()),
                        getSelectedTimeTag(),
                        binding.highPressureEdit.text.toString().toInt(),
                        binding.lowPressureEdit.text.toString().toInt(),
                        binding.heartRateEdit.text.toString().toInt(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            } else {
                when(0) {
                    binding.highPressureEdit.text.length ->
                        Toast.makeText(requireContext(), "입력된 최고 혈압 수치가 없습니다.", Toast.LENGTH_LONG).show()

                    binding.lowPressureEdit.text.length ->
                        Toast.makeText(requireContext(), "입력된 최저 혈압 수치가 없습니다.", Toast.LENGTH_LONG).show()

                    binding.heartRateEdit.text.length ->
                        Toast.makeText(requireContext(), "입력된 맥박 수치가 없습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.highPressureEdit.addTextChangedListener {
            examineEditTextFull()
        }

        binding.lowPressureEdit.addTextChangedListener {
            examineEditTextFull()
        }

        binding.heartRateEdit.addTextChangedListener {
            examineEditTextFull()
        }
        pressureViewModel.pressureResult.observe(this, {
            baseViewModel.setServerFinish(true)
            reload()
            parentFragmentManager.beginTransaction().remove(this).commit()
        })

        baseViewModel.buttonClickEnable.observe(this, {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        pressureViewModel.isError.observe(this) {
            baseViewModel.setServerFinish(true)
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.highPressureEditLayout.setOnClickListener {
            binding.highPressureEdit.isFocusableInTouchMode = true
            binding.highPressureEdit.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.highPressureEdit, 0)
        }

        binding.lowPressureEditLayout.setOnClickListener {
            binding.lowPressureEdit.isFocusableInTouchMode = true
            binding.lowPressureEdit.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.lowPressureEdit, 0)
        }

        binding.heartRateEditLayout.setOnClickListener {
            binding.heartRateEdit.isFocusableInTouchMode = true
            binding.heartRateEdit.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.heartRateEdit, 0)
        }

        KeyboardVisibilityEvent.setEventListener(
            requireActivity(),
            this,
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    if (isOpen) {
                        binding.timeSelectorFrame.visibility = View.GONE
                    } else {
                        binding.timeSelectorFrame.visibility = View.VISIBLE
                    }
                }
            }
        )

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog : Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpRatio(bottomSheetDialog, 80)
        }
        return dialog
    }

    fun initTimeSelector() {
        val manager = childFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun setRecordBaseTime() {
        baseViewModel.setSelectedTimeTag(when(data!!.timeTag) {
            "아침" -> 1
            "점심" -> 2
            "저녁" -> 3
            else -> 1
        })
        val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        baseViewModel.setLocalDateTime(LocalDateTime.parse(data.recordDate, localDateTimeFormatter))
    }

    fun examineEditTextFull() {
        baseViewModel.setInputAll(binding.highPressureEdit.text.length > 0 && binding.lowPressureEdit.text.length > 0 && binding.heartRateEdit.text.length > 0)
    }

    fun getSelectedTimeTag() = when(baseViewModel.selectedTimeTag.value) {
        1 -> "아침"
        2 -> "점심"
        3 -> "저녁"
        else -> "아침"
    }
}