package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class JoinButtonSelectViewModel : BaseViewModel() {

    private val _titleText = MutableLiveData<String>()
    val titleText : LiveData<String>
        get() = _titleText

    private val _selectedButton = MutableLiveData(1)
    val selectedButton : LiveData<Int>
        get() = _selectedButton

    private val _buttonClickEnable = MutableLiveData(false)
    val buttonClickEnable : LiveData<Boolean>
        get() = _buttonClickEnable

    fun setSelectedTitle(title : String) {
        _titleText.value = title
    }

    fun setSelectedButton(kind : Int) {
        _selectedButton.value = kind
    }

    fun setButtonClickEnable(work : Boolean) {
        _buttonClickEnable.value = work
    }
}