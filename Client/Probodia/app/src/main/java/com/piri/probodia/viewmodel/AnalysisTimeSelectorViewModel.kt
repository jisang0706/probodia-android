package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AnalysisTimeSelectorViewModel : ViewModel() {

    private val _selectedTimeTag = MutableLiveData<Int>()
    val selectedTimeTag : LiveData<Int>
        get() = _selectedTimeTag

    fun setSelectedTimeTag(num : Int) {
        _selectedTimeTag.value = num
    }
}