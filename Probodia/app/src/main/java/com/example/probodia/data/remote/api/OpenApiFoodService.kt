package com.example.probodia.data.remote.api

import com.example.probodia.data.remote.model.ApiFoodDto
import retrofit2.http.*

interface OpenApiFoodService {

    @GET("api/{serviceKey}/I2790/json/{pageNo}/20/DESC_KOR={name}")
    suspend fun getFoodList(
        @Path("serviceKey") serviceKey : String,
        @Path("name") name : String,
        @Path("pageNo") pageNo : Int
    ) : ApiFoodDto
}