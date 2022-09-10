package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.ApiMedicineDto
import com.example.probodia.data.remote.model.MedicineDto
import com.example.probodia.repository.OpenApiMedicineRepository
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import kotlinx.coroutines.launch

class RecordMedicineViewModel : TokenViewModel() {

    private val _medicineResult = MutableLiveData<MedicineDto.Record>()
    val medicineResult : LiveData<MedicineDto.Record>
        get() = _medicineResult

    fun postMedicine(
        preferenceRepository : PreferenceRepository,
        timeTag : String,
        medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>,
        recordDate : String
    ) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _postMedicine(accessToken, timeTag, medicineList, recordDate)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _postMedicine(accessToken, timeTag, medicineList, recordDate)
        }
    }

    suspend fun _postMedicine(accessToken : String, timeTag : String, medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>, recordDate : String) {
        _medicineResult.value = serverRepository.postMedicine(accessToken, timeTag, medicineList, recordDate)
    }

    fun putMedicine(
        preferenceRepository : PreferenceRepository,
        recordId : Int,
        timeTag : String,
        medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>,
        recordDate : String
    ) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _putMedicine(
                accessToken,
                recordId,
                timeTag,
                recordDate,
                medicineList
            )
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _putMedicine(
                accessToken,
                recordId,
                timeTag,
                recordDate,
                medicineList
            )
        }
    }

    suspend fun _putMedicine(accessToken : String, recordId : Int, timeTag : String, recordDate : String, medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>) {
        _medicineResult.value = serverRepository.putMedicine(accessToken, recordId, timeTag, recordDate, medicineList)
    }
}