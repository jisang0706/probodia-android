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

class RecordMedicineViewModel : ViewModel() {

    val serverrepository = ServerRepository()

    private val _medicineResult = MutableLiveData<MedicineDto>()
    val medicineResult : LiveData<MedicineDto>
        get() = _medicineResult

    fun postMedicine(
        preferenceRepository : PreferenceRepository,
        timeTag : String,
        medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>,
        recordDate : String
    ) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _medicineResult.value = serverrepository.postMedicine(accessToken, timeTag, medicineList, recordDate)
    }
}