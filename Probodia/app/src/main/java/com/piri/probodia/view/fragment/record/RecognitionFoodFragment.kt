package com.piri.probodia.view.fragment.record

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.R
import com.piri.probodia.data.remote.body.PostMealBody
import com.piri.probodia.databinding.FragmentRecognitionFoodBinding
import com.piri.probodia.viewmodel.RecognitionFoodViewModel
import com.piri.probodia.viewmodel.factory.RecognitionFoodViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class RecognitionFoodFragment(val addMealItem : (item : PostMealBody.PostMealItem) -> Unit, val foodImage : Bitmap?, val foodNames : List<String>) : BaseBottomSheetDialogFragment() {

    private lateinit var binding : FragmentRecognitionFoodBinding

    private lateinit var viewModel : RecognitionFoodViewModel
    private lateinit var viewModelFactory : RecognitionFoodViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recognition_food, container, false)

        binding.foodImage.setImageBitmap(foodImage)

        viewModelFactory = RecognitionFoodViewModelFactory(foodNames)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecognitionFoodViewModel::class.java)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        viewModel.selectedFood.observe(viewLifecycleOwner, {
            binding.food1Btn.setBackgroundResource(R.drawable.white_1_background)
            binding.food1Btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
            binding.food2Btn.setBackgroundResource(R.drawable.white_1_background)
            binding.food2Btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))
            binding.food3Btn.setBackgroundResource(R.drawable.white_1_background)
            binding.food3Btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_700))

            when(it) {
                0 -> {
                    binding.food1Btn.setBackgroundResource(R.drawable.primary_100_1_background)
                    binding.food1Btn.setTextColor(Color.WHITE)
                }
                1 -> {
                    binding.food2Btn.setBackgroundResource(R.drawable.primary_100_1_background)
                    binding.food2Btn.setTextColor(Color.WHITE)
                }
                2 -> {
                    binding.food3Btn.setBackgroundResource(R.drawable.primary_100_1_background)
                    binding.food3Btn.setTextColor(Color.WHITE)
                }
            }
        })

        binding.enterBtn.setOnClickListener {
            val fragment = SearchFoodFragment(R.integer.record, ::applyItem, viewModel.foodNameResult.value!![viewModel.selectedFood.value!!])
            fragment.show(childFragmentManager, fragment.tag)
        }

        binding.searchBtn.setOnClickListener {
            val fragment = SearchFoodFragment(R.integer.record, ::applyItem)
            fragment.show(childFragmentManager, fragment.tag)
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

    fun applyItem(item : PostMealBody.PostMealItem) {
        addMealItem(item)
        parentFragmentManager.beginTransaction().remove(this).commit()
    }
}