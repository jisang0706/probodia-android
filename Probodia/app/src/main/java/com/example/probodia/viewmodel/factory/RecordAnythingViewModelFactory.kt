package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.viewmodel.RecordAnythingViewModel

class RecordAnythingViewModelFactory(private val preferenceRepository: PreferenceRepository, private val num : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordAnythingViewModel::class.java)) {
            return RecordAnythingViewModel(preferenceRepository, num) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}