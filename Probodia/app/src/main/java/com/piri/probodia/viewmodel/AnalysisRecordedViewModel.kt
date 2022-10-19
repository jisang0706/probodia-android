package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.widget.utils.Convert
import com.piri.probodia.widget.utils.TimeTag

class AnalysisRecordedViewModel : ViewModel() {

    private val _firGlucoseRange = MutableLiveData<Int>()
    val firGlucoseRange : LiveData<Int>
        get() = _firGlucoseRange

    private val _secGlucoseRange = MutableLiveData<Int>()
    val secGlucoseRange : LiveData<Int>
        get() = _secGlucoseRange

    private val _thrGlucoseRange = MutableLiveData<Int>()
    val thrGlucoseRange : LiveData<Int>
        get() = _thrGlucoseRange

    fun setRecordedRange(items : MutableList<TodayRecord.AllData>) {
        val recordedList = MutableList<Int>(3, init = { _ -> 0})

        for(item in items) {
            recordedList[
                Convert.timeTagToInt(item.record.timeTag.split(' ')[0]) - 6
            ]++
        }

        _firGlucoseRange.value = recordedList[0]
        _secGlucoseRange.value = recordedList[1]
        _thrGlucoseRange.value = recordedList[2]
    }
}