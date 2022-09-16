package com.example.probodia.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.probodia.R
import com.example.probodia.adapter.SearchAdapter
import com.example.probodia.data.remote.body.PostMealBody
import com.example.probodia.data.remote.model.ApiItemName
import com.example.probodia.data.remote.model.FoodDto
import com.example.probodia.databinding.FragmentSearchFoodBinding
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.view.activity.RecognitionFoodActivity
import com.example.probodia.view.activity.RecordMealActivity
import com.example.probodia.view.activity.SearchFoodDetailActivity
import com.example.probodia.viewmodel.SearchFoodViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchFoodFragment(val addItem : (item : PostMealBody.PostMealItem) -> Unit, val foodName : String) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchFoodBinding

    private lateinit var viewModel : SearchFoodViewModel

    private lateinit var listAdapter : SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_food, container, false)

        viewModel = ViewModelProvider(this).get(SearchFoodViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        listAdapter = SearchAdapter()
        binding.foodRv.adapter = listAdapter
        binding.foodRv.layoutManager = LinearLayoutManager(requireContext())

        if (foodName != "") {
            binding.foodEdittext.setText(foodName, TextView.BufferType.EDITABLE)
            viewModel.getFood(true, PreferenceRepository(requireContext()), binding.foodEdittext.text.toString(), 1)
        }

        binding.foodEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.getFood(true, PreferenceRepository(requireContext()), binding.foodEdittext.text.toString(), 1)
            }
        })

        viewModel.result.observe(this, {
            if (it.first) {
                listAdapter.resetDataSet()
            }

            listAdapter.addDataSet(it.second.data as MutableList<ApiItemName>)
            listAdapter.notifyDataSetChanged()
        })

        listAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val item = listAdapter.dataSet[position] as FoodDto.FoodItem
                if (item != null) {
                    val fragment = SearchFoodDetailFragment(::applyItem, item.foodId)
                    fragment.show(childFragmentManager, fragment.tag)
                } else {
                    Toast.makeText(requireContext(), "음식 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
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

    fun applyItem(item : PostMealBody.PostMealItem) {
        addItem(item)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}