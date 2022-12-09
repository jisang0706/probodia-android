package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.adapter.SearchAdapter
import com.piri.probodia.data.remote.model.ApiItemName
import com.piri.probodia.data.remote.model.ApiMedicineDto
import com.piri.probodia.databinding.FragmentSearchMedicineBinding
import com.piri.probodia.viewmodel.SearchMedicineViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import java.util.regex.Pattern

class SearchMedicineFragment(val setMedicine : (item : ApiMedicineDto.Body.MedicineItem, position : Int) -> Unit, val basePosition : Int) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchMedicineBinding
    private lateinit var viewModel : SearchMedicineViewModel
    private lateinit var listAdapter : SearchAdapter

    private var pageNo = 1
    private var lastItemId = ""

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
            Log.e("MEDICINE", "SEARCH")
            if (it.first) {
                listAdapter.resetDataSet()
                listAdapter.notifyDataSetChanged()
            }

            if (it.second.body.items != null && it.second.body.items.isNotEmpty()) {
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
                val name = "${binding.medicineEdittext.text}"
                if (Pattern.matches("^[a-zA-Z0-9가-힣]+$", name)) {
                    viewModel.setMedicineName(name)
                }
            }
        })

        binding.medicineEdittext.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                when (target) {
                    DrawablePosition.LEFT -> {
                        viewModel.setMedicineName("${binding.medicineEdittext.text}")
                    }
                }
            }

        })

        viewModel.medicineName.observe(this, {
            pageNo = 1
            lastItemId = ""
            viewModel.getMedicine(true, it, pageNo)
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

                val listLastItem : ApiMedicineDto.Body.MedicineItem? =
                    listAdapter.getLastItem() as ApiMedicineDto.Body.MedicineItem?

                if (listLastItem != null && lastVisible >= totalCount - 1 &&
                    lastItemId != listLastItem.item_seq) {
                    lastItemId = listLastItem.item_seq
                    viewModel.medicineName.value?.let { viewModel.getMedicine(false, it, ++pageNo) }
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