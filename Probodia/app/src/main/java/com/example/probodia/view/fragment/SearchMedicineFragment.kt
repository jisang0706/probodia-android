package com.example.probodia.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.probodia.R
import com.example.probodia.adapter.SearchAdapter
import com.example.probodia.data.remote.model.ApiItemName
import com.example.probodia.data.remote.model.ApiMedicineDto
import com.example.probodia.databinding.FragmentSearchMedicineBinding
import com.example.probodia.viewmodel.SearchMedicineViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchMedicineFragment(val setMedicine : (item : ApiMedicineDto.Body.MedicineItem, position : Int) -> Unit, val basePosition : Int) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchMedicineBinding
    private lateinit var viewModel : SearchMedicineViewModel
    private lateinit var listAdapter : SearchAdapter

    private var pageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_medicine, container, false)

        viewModel = ViewModelProvider(this).get(SearchMedicineViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        listAdapter = SearchAdapter()
        binding.medicineRv.adapter = listAdapter
        binding.medicineRv.layoutManager = LinearLayoutManager(requireContext())

        viewModel.result.observe(this, {
            if (it.first) {
                listAdapter.resetDataSet()
                listAdapter.notifyDataSetChanged()
            }

            if (it.second.body.items != null) {
                listAdapter.addDataSet(it.second.body.items as MutableList<ApiItemName>)
                listAdapter.notifyDataSetChanged()
            }
        })

        binding.medicineEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                pageNo = 1
                viewModel.getMedicine(true, binding.medicineEdittext.text.toString(), pageNo)
            }

        })

        listAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val item = listAdapter.getItem(position) as ApiMedicineDto.Body.MedicineItem?
                if (item != null) {
                    setMedicine(item, basePosition)
                    binding.cancelBtn.callOnClick()
                } else {
                    Toast.makeText(requireContext(), "약 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.medicineRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager : LinearLayoutManager = binding.medicineRv.layoutManager as LinearLayoutManager
                val totalCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisible >= totalCount - 1) {
                    viewModel.getMedicine(false, binding.medicineEdittext.text.toString(), ++pageNo)
                }
            }
        })

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
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
}