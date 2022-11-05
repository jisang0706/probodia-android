package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.piri.probodia.R
import com.piri.probodia.adapter.MedicineAddAdapter
import com.piri.probodia.data.remote.model.ApiMedicineDto
import com.piri.probodia.data.remote.model.MedicineDto
import com.piri.probodia.databinding.FragmentRecordMedicineBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordAnythingViewModel
import com.piri.probodia.viewmodel.RecordMedicineViewModel
import com.piri.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordMedicineFragment(val reload : () -> Unit, val recordType : Int, val data : MedicineDto.Record?) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecordMedicineBinding

    private lateinit var medicineViewModel : RecordMedicineViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var listAdapter : MedicineAddAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initTimeSelector()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record_medicine, container, false)

        medicineViewModel = ViewModelProvider(this).get(RecordMedicineViewModel::class.java)
        binding.medicineVm = medicineViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(3)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        listAdapter = MedicineAddAdapter()
        binding.medicineAddRv.adapter = listAdapter
        binding.medicineAddRv.layoutManager = LinearLayoutManager(requireContext())

        listAdapter.setOnItemButtonClickListener(object  : MedicineAddAdapter.OnItemButtonClickListener {
            override fun onItemDeleteClick(position: Int) {
                listAdapter.deleteItem(position)
                listAdapter.notifyDataSetChanged()
                baseViewModel.setInputAll(listAdapter.checkItemComplete())
            }

            override fun onItemSearchClick(position: Int) {
                val fragment = SearchMedicineFragment(::setMedicine, position)
                fragment.show(childFragmentManager, fragment.tag)
            }

            override fun onItemPlusClick() {
                listAdapter.addItem(
                    ApiMedicineDto.Body.MedicineItem(
                    "",
                    "약 선택하기",
                    "",
                    "",
                    "",
                    ""
                ))
                listAdapter.notifyDataSetChanged()
                binding.medicineAddRv.scrollToPosition(listAdapter.itemCount - 1)
                baseViewModel.setInputAll(listAdapter.checkItemComplete())
            }
        })

        setRecordBaseTime()

        if (recordType == 1) {
            for(i in 0 until data!!.medicineDetails.size) {
                listAdapter.addItem(ApiMedicineDto.Body.MedicineItem(
                    data.medicineDetails[i].medicineId,
                    data.medicineDetails[i].medicineName,
                    "",
                    "",
                    "",
                    ""
                ))
                listAdapter.setItemUnit(i, data.medicineDetails[i].medicineCnt)
            }
            listAdapter.notifyDataSetChanged()
            baseViewModel.setInputAll(listAdapter.checkItemComplete())
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                baseViewModel.setServerFinish(false)
                if (recordType == 1) {
                    medicineViewModel.putMedicine(
                        PreferenceRepository(requireContext()),
                        data!!.recordId,
                        getSelectedTimeTag(),
                        listAdapter.getList(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                } else {
                    medicineViewModel.postMedicine(
                        PreferenceRepository(requireContext()),
                        getSelectedTimeTag(),
                        listAdapter.getList(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            } else {
                Toast.makeText(requireContext(), "입력된 투약 기록이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        baseViewModel.buttonClickEnable.observe(this, {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })

        medicineViewModel.medicineResult.observe(this, {
            baseViewModel.setServerFinish(true)
            reload()
            parentFragmentManager.beginTransaction().remove(this).commit()
        })

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        medicineViewModel.isError.observe(this) {
            baseViewModel.setServerFinish(true)
            Toast.makeText(requireContext(), "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }

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

    fun getSelectedTimeTag(): String {
        return when(baseViewModel.selectedTimeTag.value) {
            1 -> "아침"
            2 -> "점심"
            3 -> "저녁"
            else -> "아침"
        }
    }

    fun setMedicine(item : ApiMedicineDto.Body.MedicineItem, position : Int) {
        if (position != -1) {
            listAdapter.setItem(position, item)
            listAdapter.notifyDataSetChanged()
            baseViewModel.setInputAll(listAdapter.checkItemComplete())
        }
    }
}