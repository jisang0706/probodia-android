package com.example.probodia.view.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.RecordDetailAdapter
import com.example.probodia.data.remote.model.*
import com.example.probodia.databinding.FragmentRecordDetailBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.ItemRecordDetailDataViewModel
import com.example.probodia.viewmodel.RecordDetailViewModel
import com.example.probodia.viewmodel.factory.ItemRecordDetailDataViewModelFactory
import com.example.probodia.viewmodel.factory.RecordDetailViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecordDetailFragment(val data : RecordDatasBase, val reload : () -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecordDetailBinding
    private lateinit var viewModel : RecordDetailViewModel
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private var listAdapter : RecordDetailAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_detail, container, false)

        viewModel = ViewModelProvider(this, RecordDetailViewModelFactory(data.type)).get(RecordDetailViewModel::class.java)
        binding.vm = viewModel

        listAdapter = when(data.type) {
            "SUGAR" -> getGlucoseListAdapter(data as GlucoseDto)
            "PRESSURE" -> getPressureListAdapter(data as PressureDto)
            "MEDICINE" -> getMedicineListAdapter(data as MedicineDto)
            "MEAL" -> getMealListAdapter(data as MealDto)
            else -> null
        }
        binding.recordDetailRv.adapter = listAdapter
        binding.recordDetailRv.layoutManager = LinearLayoutManager(context)

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        binding.deleteBtn.setOnClickListener {
            val recordId : Int = when(data.type) {
                "SUGAR" -> (data as GlucoseDto).record.recordId
                "PRESSURE" -> (data as PressureDto).record.recordId
                "MEDICINE" -> (data as MedicineDto).record.recordId
                "MEAL" -> (data as MealDto).record.recordId
                else -> 0
            }
            viewModel.deleteRecord(PreferenceRepository(requireContext()), recordId)
        }

        viewModel.deleteResult.observe(this, {
            reload()
            parentFragmentManager.beginTransaction().remove(this).commit()
        })

        return binding.root
    }

    companion object {
        const val TAG = "BottomSheetFragment"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog : Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            setUpRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setUpRatio(bottomSheetDialog : BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from<View>(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheeetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        bottomSheet.setBackgroundResource(R.drawable.white_top_3_background)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheeetDialogDefaultHeight() : Int {
        return getWindowHeight() * 50 / 100
    }

    private fun getWindowHeight() : Int {
        return (context as Activity?)!!.getSystemService(WindowManager::class.java)
            .currentWindowMetrics.bounds.height()
    }

    private fun getGlucoseListAdapter(glucoseDto : GlucoseDto) : RecordDetailAdapter{
        val itemDatas = listOf(
            ItemRecordDetailDataDto(
                glucoseDto.record.timeTag,
                "mg/dL",
                glucoseDto.record.glucose
            )
        )
        return RecordDetailAdapter("", itemDatas)
    }

    private fun getPressureListAdapter(pressureDto : PressureDto) : RecordDetailAdapter{
        val itemDatas = listOf(
            ItemRecordDetailDataDto(
                "최고 혈압",
                "mmHg",
                pressureDto.record.maxPressure
            ),

            ItemRecordDetailDataDto(
                "최저 혈압",
                "mmHg",
                pressureDto.record.minPressure
            ),

            ItemRecordDetailDataDto(
                "맥박",
                "회/분",
                pressureDto.record.heartRate
            )
        )
        return RecordDetailAdapter(pressureDto.record.timeTag, itemDatas)
    }

    fun getMedicineListAdapter(medicineDto : MedicineDto) : RecordDetailAdapter {
        val itemDatas : MutableList<ItemRecordDetailDataDto> = mutableListOf()
        for(medicine in medicineDto.record.medicineDetails) {
            itemDatas.add(
                ItemRecordDetailDataDto(
                    medicine.medicineName,
                    "unit",
                    medicine.medicineCnt
                )
            )
        }
        return RecordDetailAdapter(medicineDto.record.timeTag, itemDatas)
    }

    fun getMealListAdapter(mealDto : MealDto) : RecordDetailAdapter{
        val itemDatas : MutableList<ItemRecordDetailDataDto> = mutableListOf()
        for(meal in mealDto.record.mealDetails) {
            itemDatas.add(
                ItemRecordDetailDataDto(
                    meal.foodName,
                    "g",
                    meal.quantity
                )
            )
        }
        return RecordDetailAdapter(mealDto.record.timeTag, itemDatas)
    }
}