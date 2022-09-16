package com.example.probodia.repository

import android.util.Log
import com.example.probodia.data.remote.api.AIServerService
import com.example.probodia.data.remote.api.RetrofitAIServerInstance
import com.example.probodia.data.remote.body.GetImageFoodBody
import com.example.probodia.data.remote.model.FoodNamesDto

class AIServerRepository {

    private val client = RetrofitAIServerInstance.getInstance().create(AIServerService::class.java)

    suspend fun getImageFood(apiToken : String, filename : String): FoodNamesDto =
        client.getImageFood("Bearer $apiToken", GetImageFoodBody("food/${filename}"))
}