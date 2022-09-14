package com.example.probodia.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.R
import com.example.probodia.data.remote.model.GlucoseDto
import com.example.probodia.databinding.FragmentRecordGlucoseBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordGlucoseViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

        if (recordType == 1) {
            baseViewModel.setSelectedTimeTag(when(data!!.timeTag) {
                "아침 식전" -> 1
                "점심 식전" -> 2
                "저녁 식전" -> 3
                "아침 식후" -> 4
                "점심 식후" -> 5
                "저녁 식후" -> 6
                else -> 1
            })
            binding.glucoseEdit.setText(data!!.glucose.toString())
            baseViewModel.setButtonClickEnable(binding.glucoseEdit.text.length!! > 0)
            val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            baseViewModel.setLocalDateTime(LocalDateTime.parse(data!!.recordDate, localDateTimeFormatter))
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
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
            baseViewModel.setButtonClickEnable(it?.length!! > 0)
        }

        glucoseViewModel.glucoseResult.observe(this, {
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
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {
        const val TAG = "BottomSheetFragment"
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