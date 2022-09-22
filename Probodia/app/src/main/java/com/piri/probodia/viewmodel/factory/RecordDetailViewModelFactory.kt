package com.piri.probodia.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.viewmodel.RecordDetailViewModel
import java.lang.IllegalArgumentException

class RecordDetailViewModelFactory(private val recordType : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordDetailViewModel::class.java)) {
            return RecordDetailViewModel(recordType) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel class")
    }
}