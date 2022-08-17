package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordMealViewModel
import java.lang.IllegalArgumentException

class RecordMealViewModelFactory(private val preferenceRepository : PreferenceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordMealViewModel::class.java)) {
            return RecordMealViewModel(preferenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}