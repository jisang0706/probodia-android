package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.probodia.data.remote.model.MedicineDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository

class RecordMedicineViewModel : ViewModel() {

    val serverRepository = ServerRepository()

    private val _medicineResult = MutableLiveData<MedicineDto>()
    val medicineResult : LiveData<MedicineDto>
        get() = _medicineResult
}