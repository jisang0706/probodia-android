package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.piri.probodia.R
import com.piri.probodia.adapter.SearchAdapter
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.data.remote.model.ApiItemName
import com.piri.probodia.data.remote.model.FoodDto
import com.piri.probodia.databinding.FragmentSearchFoodBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.SearchFoodViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import com.piri.probodia.data.db.FoodDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SearchFoodFragment(val kind : Int, val addItem : (item : PostMealBody.PostMealItem) -> Unit = {}, val foodName : String = "") : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchFoodBinding

    private lateinit var viewModel : SearchFoodViewModel

    private lateinit var listAdapter : SearchAdapter

    private var pageNo = 1
    private var lastItemId = ""

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
        } else {
            viewModel.getFoodDb(FoodDatabase.getDatabase(requireContext()))
        }

        binding.foodEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val name = "${binding.foodEdittext.text}"
                if (name.isNotEmpty() && Pattern.matches("^[a-zA-Z0-9가-힣]+$", name)) {
                    viewModel.setFoodName(name)
                }
                else if (name.isEmpty()) {
                    viewModel.getFoodDb(FoodDatabase.getDatabase(requireContext()))
                }
            }
        })

        binding.foodEdittext.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                when (target) {
                    DrawablePosition.LEFT -> {
                        viewModel.setFoodName("${binding.foodEdittext.text}")
                    }
                }
            }

        })

        viewModel.result.observe(this, {
            if (it.first) {
                listAdapter.resetDataSet()
                listAdapter.notifyDataSetChanged()
            }

            if (it.second.data != null && it.second.data.isNotEmpty() && binding.foodEdittext.text.isNotEmpty()) {
                listAdapter.addDataSet(it.second.data as MutableList<ApiItemName>)
                listAdapter.notifyDataSetChanged()
            }
        })

        listAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
            override fun onItemClick(v: View, position: Int) {
                val item = listAdapter.dataSet[position] as FoodDto.FoodItem
                if (item != null) {
                    viewModel.saveFoodDb(FoodDatabase.getDatabase(requireContext()), item)
                    val fragment = SearchFoodDetailFragment(kind, item.foodId, ::applyItem)
                    fragment.show(childFragmentManager, fragment.tag)
                } else {
                    Toast.makeText(requireContext(), "음식 정보를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.foodname.observe(this) {
            pageNo = 1
            lastItemId = ""
            viewModel.getFood(
                true,
                PreferenceRepository(requireContext()),
                it,
                pageNo
            )
        }

        binding.foodRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager : LinearLayoutManager = binding.foodRv.layoutManager as LinearLayoutManager
                val totalCount = layoutManager.itemCount
                val lastVisible = layoutManager.findLastCompletelyVisibleItemPosition()

                val listLastItem : FoodDto.FoodItem? =
                    listAdapter.getLastItem() as FoodDto.FoodItem?

                if (listLastItem != null && lastVisible >= totalCount - 1 &&
                    lastItemId != listLastItem.foodId) {
                    lastItemId = listLastItem.foodId
                    viewModel.foodname.value?.let {
                        viewModel.getFood(false, PreferenceRepository(requireContext()),
                            it, ++pageNo)
                    }
                }
            }
        })

        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        viewModel.foodDbResult.observe(viewLifecycleOwner) { entityList ->
            CoroutineScope(Dispatchers.Main).launch {
                val foodList = buildList<FoodDto.FoodItem> {
                    for (item in entityList) {
                        add(FoodDto.FoodItem(
                            item.foodName,
                            item.foodId
                        ))
                    }
                }

                listAdapter.resetDataSet()
                listAdapter.notifyDataSetChanged()

                if (foodList != null && foodList.isNotEmpty()) {
                    listAdapter.addDataSet(foodList as MutableList<ApiItemName>)
                    listAdapter.notifyDataSetChanged()
                }
            }
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