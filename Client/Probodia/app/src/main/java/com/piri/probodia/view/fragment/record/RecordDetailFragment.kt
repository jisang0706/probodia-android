package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.piri.probodia.R
import com.piri.probodia.adapter.RecordDetailAdapter
import com.piri.probodia.data.remote.model.*
import com.piri.probodia.databinding.FragmentRecordDetailBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordDetailViewModel
import com.piri.probodia.viewmodel.factory.RecordDetailViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class RecordDetailFragment(val data : RecordDatasBase, val reload : () -> Unit) : BaseBottomSheetDialogFragment() {

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

        val datetime : List<String> = when(data.type) {
            "SUGAR" -> (data as GlucoseDto).record.recordDate.split(" ")
            "PRESSURE" -> (data as PressureDto).record.recordDate.split(" ")
            "MEDICINE" -> (data as MedicineDto).record.recordDate.split(" ")
            "MEAL" -> (data as MealDto).record.recordDate.split(" ")
            else -> listOf("", "")
        }

        val YMD : List<String> = datetime[0].split("-")
        val date : LocalDate = LocalDate.of(YMD[0].toInt(), YMD[1].toInt(), YMD[2].toInt())
        val dayOfWeek : String = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)

        val time = buildList {
            add("오전")
            for(obj in datetime[1].split(":")) {
                add(obj)
            }
        }.toMutableList()

        if (time[1].toInt() > 12) {
            time[0] = "오후"
            time[1] = "${time[1].toInt() - 12}"
        } else {
            time[1] = "${time[1].toInt()}"
        }
        if (time[1].toInt() == 0) {
            time[1] = "12"
        }

        time[2] = "${time[2].toInt()}"

        binding.recordDateText.text = "${YMD[0].substring(2)}년 ${YMD[1]}월 ${YMD[2]}일 ($dayOfWeek)"
        binding.recordTimeText.text = "${time[0]} ${time[1]}시 ${time[2]}분"

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

        binding.editBtn.setOnClickListener {
            when(data.type) {
                "SUGAR" -> {
                    val fragment = RecordGlucoseFragment(::finishAndReload, 1, (data as GlucoseDto).record)
                    fragment.show(childFragmentManager, fragment.tag)
                }
                "PRESSURE" -> {
                    val fragment = RecordPressureFragment(::finishAndReload, 1, (data as PressureDto).record)
                    fragment.show(childFragmentManager, fragment.tag)
                }
                "MEDICINE" -> {
                    val fragment = RecordMedicineFragment(::finishAndReload, 1, (data as MedicineDto).record)
                    fragment.show(childFragmentManager, fragment.tag)
                }
                "MEAL" -> {
                    val fragment = RecordMealFragment(::finishAndReload, 1, (data as MealDto).record)
                    fragment.show(childFragmentManager, fragment.tag)
                }
            }
        }

        viewModel.deleteResult.observe(this, {
            reload()
            parentFragmentManager.beginTransaction().remove(this).commit()
        })

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val intent = result.data
            if (intent != null) {
                if (mutableListOf(
                        R.integer.record_glucose_result_code,
                        R.integer.record_pressure_result_code,
                        R.integer.record_medicine_result_code,
                        R.integer.record_meal_result_code,
                ).any{ it == result.resultCode}) {
                    if (intent.getBooleanExtra("RELOAD", false)) {
                        finishAndReload()
                    }
                }
            }
        }

        viewModel.isError.observe(requireActivity()) {
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
            setUpRatio(bottomSheetDialog, 50)
        }
        return dialog
    }

    private fun getGlucoseListAdapter(glucoseDto : GlucoseDto) : RecordDetailAdapter{
        val itemDatas = listOf(
            ItemRecordDetailDataDto(
                glucoseDto.record.timeTag,
                "mg/dL",
                glucoseDto.record.glucose.toString()
            )
        )
        return RecordDetailAdapter("", itemDatas)
    }

    private fun getPressureListAdapter(pressureDto : PressureDto) : RecordDetailAdapter{
        val itemDatas = listOf(
            ItemRecordDetailDataDto(
                "최고 혈압",
                "mmHg",
                pressureDto.record.maxPressure.toString()
            ),

            ItemRecordDetailDataDto(
                "최저 혈압",
                "mmHg",
                pressureDto.record.minPressure.toString()
            ),

            ItemRecordDetailDataDto(
                "맥박",
                "회/분",
                pressureDto.record.heartRate.toString()
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
                    medicine.medicineCnt.toString()
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
                    meal.quantity.toString()
                )
            )
        }
        return RecordDetailAdapter(mealDto.record.timeTag, itemDatas)
    }

    fun finishAndReload() {
        reload()
        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}