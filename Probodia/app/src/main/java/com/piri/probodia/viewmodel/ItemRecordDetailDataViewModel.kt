package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemRecordDetailDataViewModel(
    baseDataName : String,
    baseDataUnit : String,
    baseDataText : Int
) : ViewModel() {

    val _dataName = MutableLiveData(baseDataName)
    val dataName : LiveData<String>
        get() = _dataName

    val _dataUnit = MutableLiveData(baseDataUnit)
    val dataUnit : LiveData<String>
        get() = _dataUnit

    val _dataText = MutableLiveData(baseDataText)
    val dataText : LiveData<Int>
        get() = _dataText
}