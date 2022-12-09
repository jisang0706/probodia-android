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
import com.piri.probodia.data.remote.model.GlucoseDto
import com.piri.probodia.databinding.FragmentRecordGlucoseBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordAnythingViewModel
import com.piri.probodia.viewmodel.RecordGlucoseViewModel
import com.piri.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordGlucoseFragment(val reload : () -> Unit, val recordType : Int, val data : GlucoseDto.Record?) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecordGlucoseBinding

    private lateinit var glucoseViewModel : RecordGlucoseViewModel

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_glucose, container, false)

        glucoseViewModel = ViewModelProvider(this).get(RecordGlucoseViewModel::class.java)
        binding.glucoseVm = glucoseViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(1)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        setRecordBaseTime()

        if (recordType == 1) {
            binding.glucoseEdit.setText(data!!.glucose.toString())
            baseViewModel.setInputAll(binding.glucoseEdit.text.length!! > 0)
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                baseViewModel.setServerFinish(false)
                if (recordType == 1) {
                    glucoseViewModel.putGlucose(
                        PreferenceRepository(requireContext()),
                        data!!.recordId,
                        getSelectedTimeTag(),
                        binding.glucoseEdit.text.toString().toInt(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                } else {
                    glucoseViewModel.postGlucose(
                        PreferenceRepository(requireContext()),
                        getSelectedTimeTag(),
                        binding.glucoseEdit.text.toString().toInt(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            } else {
                Toast.makeText(requireContext(), "입력된 혈당 수치가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        binding.glucoseEdit.addTextChangedListener {
            baseViewModel.setInputAll(it?.length!! > 0)
        }

        glucoseViewModel.glucoseResult.observe(this, {
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

        glucoseViewModel.isError.observe(this) {
            baseViewModel.setServerFinish(true)
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        binding.glucoseEditLayout.setOnClickListener {
            binding.glucoseEdit.isFocusableInTouchMode = true
            binding.glucoseEdit.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.glucoseEdit, 0)
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
            "아침 식전" -> 1
            "점심 식전" -> 2
            "저녁 식전" -> 3
            "아침 식후" -> 4
            "점심 식후" -> 5
            "저녁 식후" -> 6
            else -> 1
        })
        val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        baseViewModel.setLocalDateTime(LocalDateTime.parse(data!!.recordDate, localDateTimeFormatter))
    }

    fun getSelectedTimeTag() = when(baseViewModel.selectedTimeTag.value) {
        1 -> "아침 식전"
        2 -> "점심 식전"
        3 -> "저녁 식전"
        4 -> "아침 식후"
        5 -> "점심 식후"
        6 -> "저녁 식후"
        else -> "아침 식전"
    }
}