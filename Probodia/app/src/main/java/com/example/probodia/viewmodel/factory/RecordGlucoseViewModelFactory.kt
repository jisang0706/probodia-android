package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordGlucoseViewModel

class RecordGlucoseViewModelFactory(private val preferenceRepository: PreferenceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordGlucoseViewModel::class.java)) {
            return RecordGlucoseViewModel() as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}