package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.MedicineAddAdapter
import com.example.probodia.data.remote.model.ApiMedicineDto
import com.example.probodia.data.remote.model.MedicineDto
import com.example.probodia.databinding.ActivityRecordMedicineBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.RecordDetailFragment
import com.example.probodia.view.fragment.RecordFragment
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordMedicineViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordMedicineActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordMedicineBinding

    private lateinit var medicineViewModel : RecordMedicineViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var listAdapter : MedicineAddAdapter

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    private var recordType = 0
    private lateinit var data : MedicineDto.Record

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        recordType = intent.getIntExtra("RECORDTYPE", 0)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_medicine)

        medicineViewModel = ViewModelProvider(this).get(RecordMedicineViewModel::class.java)
        binding.medicineVm = medicineViewModel

        baseViewModelFactory = RecordAnythingViewModelFactory(3)
        baseViewModel = ViewModelProvider(this, baseViewModelFactory).get(RecordAnythingViewModel::class.java)
        binding.baseVm = baseViewModel

        binding.lifecycleOwner = this

        initTimeSelector()

        listAdapter = MedicineAddAdapter()
        binding.medicineAddRv.adapter = listAdapter
        binding.medicineAddRv.layoutManager = LinearLayoutManager(applicationContext)

        listAdapter.setOnItemButtonClickListener(object : MedicineAddAdapter.OnItemButtonClickListener {
            override fun onItemDeleteClick(position: Int) {
                listAdapter.deleteItem(position)
                listAdapter.notifyDataSetChanged()
                baseViewModel.setButtonClickEnable(listAdapter.checkItemComplete())
            }

            override fun onItemSearchClick(position: Int) {
                val intent = Intent(applicationContext, SearchMedicineActivity::class.java)
                intent.putExtra("POSITION", position)
                activityResultLauncher.launch(intent)
            }

            override fun onItemPlusClick() {
                listAdapter.addItem(ApiMedicineDto.Body.MedicineItem(
                    "",
                    "약 선택하기",
                "",
                    "",
                    "",
                    ""
                ))
                listAdapter.notifyDataSetChanged()
                binding.medicineAddRv.scrollToPosition(listAdapter.itemCount - 1)
                baseViewModel.setButtonClickEnable(listAdapter.checkItemComplete())
            }
        })

        if (recordType == 1) {
            data = intent.getParcelableExtra("DATA")!!
            baseViewModel.setSelectedTimeTag(when(data.timeTag) {
                "아침" -> 1
                "점심" -> 2
                "저녁" -> 3
                else -> 1
            })
            val localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            baseViewModel.setLocalDateTime(LocalDateTime.parse(data.recordDate, localDateTimeFormatter))
            for(i in 0 until data.medicineDetails.size) {
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
            baseViewModel.setButtonClickEnable(listAdapter.checkItemComplete())
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val intent = result.data
            if (intent != null) {
                if (result.resultCode == R.integer.record_medicine_set_code) {
                    val item : ApiMedicineDto.Body.MedicineItem = intent.getParcelableExtra("SETMEDICINE")!!
                    val position : Int = intent.getIntExtra("POSITION", -1)
                    setMedicine(item, position)
                }
            }
        }

        binding.enterBtn.setOnClickListener {
            if (baseViewModel.buttonClickEnable.value!!) {
                if (recordType == 1) {
                    medicineViewModel.putMedicine(
                        PreferenceRepository(applicationContext),
                        data.recordId,
                        getSelectedTimeTag(),
                        listAdapter.getList(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                } else {
                    medicineViewModel.postMedicine(
                        PreferenceRepository(applicationContext),
                        getSelectedTimeTag(),
                        listAdapter.getList(),
                        baseViewModel.localDateTime.value!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    )
                }
            } else {
                Toast.makeText(applicationContext, "입력된 투약 기록이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }

        baseViewModel.buttonClickEnable.observe(this, Observer {
            if (it) {
                binding.enterBtn.setBackgroundResource(R.drawable.primary_100_2_background)
            } else {
                binding.enterBtn.setBackgroundResource(R.drawable.gray_300_2_background)
            }
        })

        medicineViewModel.medicineResult.observe(this, Observer {
            var resultIntent : Intent
            if (recordType == 0) {
                resultIntent = Intent(applicationContext, RecordFragment::class.java)
            } else {
                resultIntent = Intent(applicationContext, RecordDetailFragment::class.java)
            }
            resultIntent.putExtra("RELOAD", true)
            setResult(R.integer.record_medicine_result_code, resultIntent)
            finish()
        })

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        medicineViewModel.isError.observe(this) {
            Toast.makeText(applicationContext, "인터넷 연결이 불안정합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun initTimeSelector() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.commit()
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
            baseViewModel.setButtonClickEnable(listAdapter.checkItemComplete())
        }
    }
}