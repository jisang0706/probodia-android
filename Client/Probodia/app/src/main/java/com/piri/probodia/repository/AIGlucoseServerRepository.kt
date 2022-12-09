package com.piri.probodia.repository

import com.piri.probodia.data.remote.api.AIGlucoseServerService
import com.piri.probodia.data.remote.api.RetrofitAIGlucoseServerService
import com.piri.probodia.data.remote.body.FoodAllGLBody
import com.piri.probodia.data.remote.body.FoodGLBody
import com.piri.probodia.data.remote.model.FoodDetailDto

class AIGlucoseServerRepository {

    private val client = RetrofitAIGlucoseServerService.getInstance().create(AIGlucoseServerService::class.java)

    suspend fun getGL(apiToken : String, foodGLBody: FoodGLBody)
        = client.getGL("Bearer $apiToken", foodGLBody)

    suspend fun getAllGL(apiToken : String, foodAllGLBody: FoodAllGLBody)
        = client.getAllGL("Bearer $apiToken", foodAllGLBody)
}