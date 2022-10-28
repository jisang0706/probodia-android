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
import kotlin.math.max
import kotlin.math.roundToInt

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
            it.carbohydrate = max(it.carbohydrate, 0.0)
            it.fat = max(it.fat, 0.0)
            it.protein = max(it.protein, 0.0)

            setMealRange(it)

            binding.firstText.text = "${it.protein.roundToInt()}"
            binding.secondText.text = "${it.carbohydrate.roundToInt()}"
            binding.thirdText.text = "${it.fat.roundToInt()}"
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

        val protein = nutrientDto.protein.roundToInt()
        val carbohydrate = nutrientDto.carbohydrate.roundToInt()
        val fat = nutrientDto.fat.roundToInt()

        if (protein <= 0) {
            binding.secondLayout.setBackgroundResource(R.drawable.yellow_600_round_background)
            if (carbohydrate <= 0) {
                binding.thirdLayout.setBackgroundResource(R.drawable.red_600_round_background)
                if (fat <= 0) {
                    binding.firstLayout.setBackgroundResource(R.drawable.gray_300_2_background)
                }
            }
        } else {
            binding.firstLayout.setBackgroundResource(R.drawable.green_600_round_background)
            binding.secondLayout.setBackgroundResource(R.drawable.yellow_600_round_right_background)
            binding.thirdLayout.setBackgroundResource(R.drawable.red_600_round_right_background)
        }

        val thirdLayoutParams = binding.thirdLayout.layoutParams
        thirdLayoutParams.width =
            binding.firstLayout.width * fat / (if (protein + carbohydrate + fat == 0) 1 else protein + carbohydrate + fat)
        binding.thirdLayout.layoutParams = thirdLayoutParams

        val secondLayoutParams = binding.secondLayout.layoutParams
        secondLayoutParams.width =
            binding.firstLayout.width * carbohydrate / (
                if (protein + carbohydrate + fat == 0)
                    1
                else
                    protein + carbohydrate + fat
                ) + thirdLayoutParams.width
        binding.secondLayout.layoutParams = secondLayoutParams
    }
}