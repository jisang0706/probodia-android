package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.data.remote.model.FoodDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerFoodService {

    @GET("api/getFoodsByName/{foodName}")
    suspend fun getFoodList(
        @Header("Authorization") token : String,
        @Path("foodName") foodName : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ) : FoodDto

    @GET("api/detailFoodInfo/{foodId}")
    suspend fun getFoodDetail(
        @Header("Authorization") token : String,
        @Path("foodId") foodId : String
    ) : FoodDetailDto
}