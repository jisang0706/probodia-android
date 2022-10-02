package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AnalysisRangeViewModel : BaseViewModel() {

    private var _secondLayoutWidth = MutableLiveData<Int>()
    val secondLayoutWidth : LiveData<Int>
        get() = _secondLayoutWidth

    private  var _thirdLayoutWidth = MutableLiveData<Int>()
    val thirdLayoutWidth : LiveData<Int>
        get() = _thirdLayoutWidth

    fun setBaseWidth(baseWidth : Int) {
        _secondLayoutWidth.value = baseWidth / 3 * 2
        _thirdLayoutWidth.value = baseWidth / 3
    }
}