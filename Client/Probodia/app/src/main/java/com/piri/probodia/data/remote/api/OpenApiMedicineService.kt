package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.model.ApiMedicineDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApiMedicineService {

    @GET("1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01")
    suspend fun getMedicineList(
        @Query("serviceKey", encoded = true) serviceKey : String,
        @Query("item_name", encoded = true) name : String,
        @Query("pageNo", encoded = true) pageNo : Int,
        @Query("numOfRows", encoded = true) numOfRows : Int,
        @Query("type", encoded = true) type : String
    ) : ApiMedicineDto
}