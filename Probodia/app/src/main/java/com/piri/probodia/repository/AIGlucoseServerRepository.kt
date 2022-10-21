package com.piri.probodia.repository

import com.piri.probodia.data.remote.api.AIGlucoseServerService
import com.piri.probodia.data.remote.api.RetrofitAIGlucoseServerService
import com.piri.probodia.data.remote.model.FoodDetailDto

class AIGlucoseServerRepository {

    private val client = RetrofitAIGlucoseServerService.getInstance().create(AIGlucoseServerService::class.java)

    suspend fun getGL(apiToken : String, foodDetailBody : FoodDetailDto)
        = client.getGL("Bearer $apiToken", foodDetailBody)
}