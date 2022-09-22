package com.piri.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.viewmodel.RecordAnythingViewModel

class RecordAnythingViewModelFactory(private val num : Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordAnythingViewModel::class.java)) {
            return RecordAnythingViewModel(num) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}