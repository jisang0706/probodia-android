package com.piri.probodia.repository

import android.util.Log
import com.piri.probodia.BuildConfig
import com.piri.probodia.data.remote.api.OpenApiMedicineService
import com.piri.probodia.data.remote.api.RetrofitOpenApiMedicineInstance
import com.piri.probodia.data.remote.model.ApiMedicineDto

class OpenApiMedicineRepository {

    private val client = RetrofitOpenApiMedicineInstance.getInstance().create(OpenApiMedicineService::class.java)

    suspend fun getMedicineList(name : String, pageNo : Int) : ApiMedicineDto {
        return client.getMedicineList(
            BuildConfig.OPEN_API_MEDICINE_KEY,
            name,
            pageNo,
            20,
            "json"
        )
    }
}