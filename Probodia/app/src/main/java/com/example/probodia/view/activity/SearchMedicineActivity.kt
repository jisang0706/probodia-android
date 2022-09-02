package com.example.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.SearchAdapter
import com.example.probodia.data.remote.model.ApiItemName
import com.example.probodia.databinding.ActivitySearchMedicineBinding
import com.example.probodia.viewmodel.SearchMedicineViewModel

class SearchMedicineActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchMedicineBinding
    private lateinit var viewModel : SearchMedicineViewModel
    private lateinit var listAdapter : SearchAdapter
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(
            applicationContext, R.color.alpha_30
        )

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_medicine)
        viewModel = ViewModelProvider(this).get(SearchMedicineViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        listAdapter = SearchAdapter()
        binding.medicineRv.adapter = listAdapter
        binding.medicineRv.layoutManager = LinearLayoutManager(applicationContext)

        viewModel.result.observe(this, Observer {
            if (it.first) {
                listAdapter.resetDataSet()
            }

            if (it.second.body.items != null) {
                listAdapter.addDataSet(it.second.body.items as MutableList<ApiItemName>)
                listAdapter.notifyDataSetChanged()
            }
        })

        binding.medicineEdittext.addTextChangedListener {
            viewModel.getMedicine(true, binding.medicineEdittext.text.toString(), 1)
        }

        listAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {

            }
        })

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}