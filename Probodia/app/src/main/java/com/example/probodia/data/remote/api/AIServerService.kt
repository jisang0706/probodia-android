package com.example.probodia.data.remote.api

import com.example.probodia.data.remote.body.GetImageFoodBody
import com.example.probodia.data.remote.model.FoodNamesDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AIServerService {

    @POST("predict")
    suspend fun getImageFood(
        @Body getImageFoodBody : GetImageFoodBody
    ) : FoodNamesDto
}