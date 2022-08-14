package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordTodayViewModel

class RecordTodayViewModelFactory(private val preferenceRepository: PreferenceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordTodayViewModel::class.java)) {
            return RecordTodayViewModel(preferenceRepository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}