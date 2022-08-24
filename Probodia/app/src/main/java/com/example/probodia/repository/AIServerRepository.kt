package com.example.probodia.repository

import android.util.Log
import com.example.probodia.data.remote.api.AIServerService
import com.example.probodia.data.remote.api.RetrofitAIServerInstance
import com.example.probodia.data.remote.body.GetImageFoodBody
import com.example.probodia.data.remote.model.FoodNamesDto

class AIServerRepository {

    private val client = RetrofitAIServerInstance.getInstance().create(AIServerService::class.java)

    suspend fun getImageFood(filename : String): FoodNamesDto {
        val img_path = "food/${filename}"
        Log.e("IMG_PATH", img_path)
        return client.getImageFood(GetImageFoodBody(img_path))
    }
}