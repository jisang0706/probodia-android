package com.example.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.probodia.viewmodel.RecordAnythingViewModel

class RecordAnythingViewModelFactory(private val num : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordAnythingViewModel::class.java)) {
            return RecordAnythingViewModel(num) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}