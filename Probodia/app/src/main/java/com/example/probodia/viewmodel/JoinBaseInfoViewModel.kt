package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JoinBaseInfoViewModel : ViewModel() {

    val _buttonClickEnable = MutableLiveData(false)
    val buttonClickEnable : LiveData<Boolean>
        get() = _buttonClickEnable
}