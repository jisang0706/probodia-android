package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.MedicineAddAdapter
import com.example.probodia.data.remote.model.ApiMedicineDto
import com.example.probodia.databinding.ActivityRecordMedicineBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.fragment.TimeSelectorFragment
import com.example.probodia.viewmodel.RecordAnythingViewModel
import com.example.probodia.viewmodel.RecordMedicineViewModel
import com.example.probodia.viewmodel.factory.RecordAnythingViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordMedicineActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordMedicineBinding

    private lateinit var medicineViewModel : RecordMedicineViewModel

    private lateinit var baseViewModel : RecordAnythingViewModel
    private lateinit var baseViewModelFactory : RecordAnythingViewModelFactory

    private lateinit var listAdapter : MedicineAddAdapter

    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

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
                baseViewModel.setButtonClickEnable(listAdapter.itemCount > 1)
            }

            override fun onItemSearchClick(position: Int) {
                val intent = Intent(applicationContext, SearchMedicineActivity::class.java)
                intent.putExtra("position", position)
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
            }
        })

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result : ActivityResult ->
            val intent = result.data
            if (intent != null) {
                if (result.resultCode == R.integer.record_medicine_set_code) {
                    val item : ApiMedicineDto.Body.MedicineItem = intent.getParcelableExtra("SETMEDICINE")!!
                }
            }
        }
    }

    fun initTimeSelector() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = TimeSelectorFragment()
        transaction.replace(R.id.time_selector_frame, fragment)
        transaction.commit()
    }
}