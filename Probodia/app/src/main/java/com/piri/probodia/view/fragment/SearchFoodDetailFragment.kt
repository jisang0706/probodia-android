package com.piri.probodia.view.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.piri.probodia.R
import com.piri.probodia.adapter.FoodInfoAdapter
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.databinding.FragmentSearchFoodDetailBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.SearchFoodDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.roundToInt

class SearchFoodDetailFragment(val kind : Int, val foodId : String, val applyItem : (item : PostMealBody.PostMealItem) -> Unit) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchFoodDetailBinding

    private lateinit var viewModel : SearchFoodDetailViewModel

    private lateinit var listAdapter : FoodInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_food_detail, container, false)

        binding.lifecycleOwner = this

        viewModel = SearchFoodDetailViewModel()
        binding.vm = viewModel

        if (kind == R.integer.search) {
            binding.enterBtn.text = "닫기"
        }

        viewModel.getFoodInfo(PreferenceRepository(requireContext()), foodId)

        viewModel.foodInfo.observe(this, {
            binding.foodNameText.text = it.name

            listAdapter = FoodInfoAdapter(getFoodInfoList(it))
            binding.foodInfoRv.adapter = listAdapter
            binding.foodInfoRv.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getFoodGL(PreferenceRepository(requireContext()), it)
        })

        viewModel.foodGL.observe(this) {
            binding.glucoseText.text = when(it.healthGL) {
                "high" -> "조금만 더 열심히 관리해보아요"
                "mid" -> "잘하고있어요"
                "low" -> "와우! 최고의 식단인걸요"
                else -> "와우! 최고의 식단인걸요"
            }

            binding.glucoseIcon.setImageResource(
                when (it.healthGL) {
                    "high" -> R.drawable.sad
                    "mid" -> R.drawable.soso
                    "low" -> R.drawable.smile
                    else -> R.drawable.smile
                }
            )
        }

        binding.quantityEdit.addTextChangedListener { edittext ->
            if (binding.quantityEdit.length() > 0 && "${binding.quantityEdit.text}"[0] == '.') {
                binding.quantityEdit.setText("0" + "${binding.quantityEdit.text}")
                binding.quantityEdit.setSelection(binding.quantityEdit.text.length)
            }

            if (edittext!!.count { it == '.' } > 1) {
                val ind = "${binding.quantityEdit.text!!}".indexOfLast { it == '.' }
                binding.quantityEdit.setText(
                    "${binding.quantityEdit.text}".substring(0, ind) +
                            "${binding.quantityEdit.text}".substring(ind + 1, binding.quantityEdit.text.length)
                )
                binding.quantityEdit.setSelection(binding.quantityEdit.text.length)
            }
        }

        binding.enterBtn.setOnClickListener {
            if (kind == R.integer.record) {
                if ("${binding.quantityEdit.text}" == "") binding.quantityEdit.setText("1")
                if ("${binding.quantityEdit.text}"[binding.quantityEdit.text.length - 1] == '.') {
                    binding.quantityEdit.setText(
                        "${binding.quantityEdit.text}".substring(
                            0,
                            "${binding.quantityEdit.text}".length - 1
                        )
                    )
                }
                val postMealItem = PostMealBody.PostMealItem(
                    viewModel.foodInfo.value!!.name,
                    foodId,
                    viewModel.foodInfo.value!!.quantityByOne * "${binding.quantityEdit.text}".toDouble(),
                    viewModel.foodInfo.value!!.calories.roundToInt(),
                    0,
                    ""
                )
                applyItem(postMealItem)
            }
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

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

    fun getFoodInfoList(item : FoodDetailDto) : MutableList<Pair<String, Double>> {
        return mutableListOf(
            Pair("탄수화물", item.carbohydrate),
            Pair("당류", item.sugars),
            Pair("단백질", item.protein),
            Pair("지방", item.fat),
            Pair("콜레스테롤", item.cholesterol),
            Pair("나트륨", item.salt)
        )
    }
}