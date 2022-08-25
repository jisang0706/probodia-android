package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.viewmodel.RecognitionFoodViewModel

class RecognitionFoodViewModelFactory(private val foodNames : List<String>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecognitionFoodViewModel::class.java)) {
            return RecognitionFoodViewModel(foodNames) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}