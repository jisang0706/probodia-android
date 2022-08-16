package com.example.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import java.time.LocalDateTime

open class RecordAnythingViewModel(val num : Int) : ViewModel() {

    open val _selectedTimeTag = MutableLiveData<Int>()
    open val selectedTimeTag : LiveData<Int>
        get() = _selectedTimeTag

    open var localDateTime : LocalDateTime = LocalDateTime.now()

    open val _buttonClickEnable = MutableLiveData(false)
    open val buttonClickEnable : LiveData<Boolean>
        get() = _buttonClickEnable

    open fun setSelectedTimeTag(kind : Int) {
        Log.e("SELECTED", kind.toString())
        _selectedTimeTag.value = kind
    }

    open fun setButtonClickEnable(work : Boolean) {
        _buttonClickEnable.value = work
    }
}