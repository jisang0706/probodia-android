package com.piri.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.RecordTodayViewModel

class RecordTodayViewModelFactory(private val preferenceRepository: PreferenceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordTodayViewModel::class.java)) {
            return RecordTodayViewModel(preferenceRepository) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}