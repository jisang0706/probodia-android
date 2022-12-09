package com.piri.probodia.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.piri.probodia.R
import com.piri.probodia.adapter.FoodInfoAdapter
import com.piri.probodia.data.remote.body.FoodGLBody
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.databinding.FragmentSearchFoodDetailBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.view.fragment.record.BaseBottomSheetDialogFragment
import com.piri.probodia.viewmodel.SearchFoodDetailViewModel

class FoodGLFragment(val foodId : String, val data : FoodDetailDto) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentSearchFoodDetailBinding
    private lateinit var viewModel : SearchFoodDetailViewModel

    private lateinit var listAdapter : FoodInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_food_detail, container, false)

        binding.lifecycleOwner = this

        viewModel = SearchFoodDetailViewModel()
        binding.vm = viewModel

        binding.enterBtn.text = "닫기"
        binding.foodGlLayout.visibility = View.GONE
        binding.quantityTitle.visibility = View.GONE
        binding.quantityLayout.visibility = View.GONE
        binding.quantityBottomLine.visibility = View.GONE

        viewModel.setFoodInfo(data)

        viewModel.foodInfo.observe(viewLifecycleOwner) {
            binding.foodNameText.text = it.name

            listAdapter = FoodInfoAdapter(getFoodInfoList(it))
            binding.foodInfoRv.adapter = listAdapter
            binding.foodInfoRv.layoutManager = LinearLayoutManager(requireContext())

            viewModel.getFoodGL(PreferenceRepository(requireContext()), FoodGLBody(
                foodId,
                it.bigCategory,
                it.smallCategory,
                it.name,
                it.quantityByOne,
                it.quantityByOneUnit,
                it.calories,
                it.carbohydrate,
                it.sugars,
                it.protein,
                it.fat,
                it.transFat,
                it.saturatedFat,
                it.cholesterol,
                it.salt
            ))

            binding.quantityText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            binding.foodInfoRv.visibility = View.VISIBLE
        }

        viewModel.foodGL.observe(this) {
            binding.glucoseText.text = when(it.main.healthGL) {
                "high" -> "조금만 더 열심히 관리해보아요"
                "mid" -> "잘하고있어요"
                "low" -> "와우! 최고의 식단인걸요"
                else -> "와우! 최고의 식단인걸요"
            }

            binding.glucoseIcon.setImageResource(
                when (it.main.healthGL) {
                    "high" -> R.drawable.sad
                    "mid" -> R.drawable.soso
                    "low" -> R.drawable.smile
                    else -> R.drawable.smile
                }
            )
        }

        binding.enterBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        viewModel.isGLError.observe(viewLifecycleOwner) {
            binding.glucoseLayout.visibility = View.GONE
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