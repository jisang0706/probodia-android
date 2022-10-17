package com.piri.probodia.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.model.NutrientDto
import com.piri.probodia.databinding.FragmentAnalysisRangeBinding
import com.piri.probodia.viewmodel.RecordAnalysisViewModel

class AnalysisMealRangeFragment : Fragment() {

    private lateinit var binding : FragmentAnalysisRangeBinding

    private lateinit var viewModel : RecordAnalysisViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisRangeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(requireParentFragment()).get(RecordAnalysisViewModel::class.java)
        binding.vm = viewModel

        binding.firstTitleText.text = "단백질"
        binding.secondTitleText.text = "탄수화물"
        binding.thirdTitleText.text = "지방"

        viewModel.mealRange.observe(viewLifecycleOwner) {
            setMealRange(it)

            binding.firstText.text = "${it.protein}"
            binding.secondText.text = "${it.carbohydrate}"
            binding.thirdText.text = "${it.fat}"
        }

        return binding.root
    }

    fun setMealRange(nutrientDto: NutrientDto) {
        if (binding.firstLayout.width > 0) {
            _setMealRange(nutrientDto)
        } else {
            binding.firstLayout.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        binding.firstLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        _setMealRange(nutrientDto)
                    }

                }
            )
        }
    }

    fun _setMealRange(nutrientDto: NutrientDto) {
        if (nutrientDto.protein <= 0) {
            binding.secondLayout.setBackgroundResource(R.drawable.yellow_600_round_background)
            if (nutrientDto.carbohydrate <= 0) {
                binding.thirdLayout.setBackgroundResource(R.drawable.red_600_round_background)
            }
        } else {
            binding.firstLayout.setBackgroundResource(R.drawable.green_600_round_background)
        }

        val thirdLayoutParams = binding.thirdLayout.layoutParams
        thirdLayoutParams.width =
            binding.firstLayout.width * nutrientDto.fat / (nutrientDto.protein + nutrientDto.carbohydrate + nutrientDto.fat)
        binding.thirdLayout.layoutParams = thirdLayoutParams

        val secondLayoutParams = binding.secondLayout.layoutParams
        secondLayoutParams.width =
            binding.firstLayout.width * nutrientDto.carbohydrate / (
                nutrientDto.protein +
                nutrientDto.carbohydrate +
                nutrientDto.fat
            ) + thirdLayoutParams.width
        binding.secondLayout.layoutParams = secondLayoutParams
    }
}