package com.piri.probodia.viewmodel

import android.view.ViewTreeObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.body.GetRecordBody
import com.piri.probodia.data.remote.model.NutrientDto
import com.piri.probodia.data.remote.model.TodayRecord
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.widget.utils.TimeTag
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecordAnalysisViewModel : BaseViewModel() {

    private val _kindEndDate = MutableLiveData(Pair(2, LocalDate.now().plusDays(1)))
    val kindEndDate : LiveData<Pair<Int, LocalDate>>
        get() = _kindEndDate

    private val _glucoseResult = MutableLiveData<MutableList<TodayRecord.AllData>>()
    val glucoseResult : LiveData<MutableList<TodayRecord.AllData>>
        get() = _glucoseResult

    private val _glucoseRange = MutableLiveData<List<Int>>()
    val glucoseRange : LiveData<List<Int>>
        get() = _glucoseRange

    private val _mealRange = MutableLiveData<NutrientDto>()
    val mealRange : LiveData<NutrientDto>
        get() = _mealRange

    fun getGlucose(preferenceRepo : PreferenceRepository, __kind : Int, __endDate : LocalDate) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getGlucose(accessToken, __kind, __endDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getGlucose(accessToken, __kind, __endDate)
        }
    }

    suspend fun _getGlucose(accessToken : String, __kind : Int, __endDate : LocalDate) {
        val __startDate : LocalDate = when(__kind) {
            1 -> __endDate.minusDays(1)
            2 -> __endDate.minusWeeks(1)
            3 -> __endDate.minusMonths(1)
            else -> __endDate.minusDays(1)
        }

        val filterType = mutableListOf("SUGAR")
        val timeTagList : MutableList<String> = TimeTag.timeTag

        val getRecordBody = GetRecordBody(
            "${__startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00",
            "${__endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00",
            filterType,
            timeTagList
        )
        val tempGlucoseResult = serverRepository.getRecords(accessToken, getRecordBody)
        if (__kind == kindEndDate.value!!.first && __endDate == kindEndDate.value!!.second) {
            _glucoseResult.value = tempGlucoseResult
        }
    }

    fun setGlucoseRange(items : MutableList<TodayRecord.AllData>) {
        var common = 0
        var high = 0
        var low = 0

        for(item in items) {
            val timeTag = item.record.timeTag.split(' ')[1]

            val start = when(timeTag) {
                "식전" -> 70
                else -> 90
            }
            val end = when(timeTag) {
                "식전" -> 130
                else -> 180
            }

            if (item.record.glucose!! < start) {
                low++
            } else if (item.record.glucose!! > end) {
                high++
            } else {
                common++
            }
        }

        _glucoseRange.value = listOf(common, low, high)
    }

    fun getMealRange(preferenceRepo : PreferenceRepository, __kind : Int, __endDate : LocalDate) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getMealRange(accessToken, __kind, __endDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _getMealRange(accessToken, __kind, __endDate)
        }
    }

    suspend fun _getMealRange(accessToken : String, __kind : Int, __endDate: LocalDate) {
        val __startDate : LocalDate = when(__kind) {
            1 -> __endDate.minusDays(1)
            2 -> __endDate.minusWeeks(1)
            3 -> __endDate.minusMonths(1)
            else -> __endDate.minusDays(1)
        }

        val tempMealRange = serverRepository.getNutrient(
            accessToken,
            "${__startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00",
            "${__endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} 00:00:00"
        )

        if (__kind == kindEndDate.value!!.first && __endDate == kindEndDate.value!!.second) {
            _mealRange.value = tempMealRange
        }
    }
}