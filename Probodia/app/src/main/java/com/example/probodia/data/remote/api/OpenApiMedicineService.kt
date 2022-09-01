package com.example.probodia.data.remote.api

import com.example.probodia.data.remote.model.ApiMedicineDto
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApiMedicineService {

    @GET("")
    suspend fun getMedicineList(
        @Query("serviceKey") serviceKey : String,
        @Query("item_name") name : String,
        @Query("pageNo") pageNo : Int,
        @Query("numOfRows") numOfRows : Int,
        @Query("type") type : String
    ) : ApiMedicineDto
}