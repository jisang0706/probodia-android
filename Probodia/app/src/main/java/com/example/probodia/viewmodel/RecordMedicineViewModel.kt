package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.probodia.data.remote.model.ApiMedicineDto
import com.example.probodia.data.remote.model.MedicineDto
import com.example.probodia.repository.OpenApiMedicineRepository
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository

class RecordMedicineViewModel : ViewModel() {

    private val openApiMedicineRepository = OpenApiMedicineRepository()

    private val _medicineResult = MutableLiveData<MedicineDto>()
    val medicineResult : LiveData<MedicineDto>
        get() = _medicineResult

    suspend fun searchMedicine(name : String, pageNo : Int) : ApiMedicineDto {
        return openApiMedicineRepository.getMedicineList(name, pageNo)
    }
}