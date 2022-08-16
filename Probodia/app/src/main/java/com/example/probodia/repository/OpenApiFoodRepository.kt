package com.example.probodia.repository

import com.example.probodia.BuildConfig
import com.example.probodia.data.remote.api.OpenApiFoodService
import com.example.probodia.data.remote.api.RetrofitOpenApiFoodInstance

class OpenApiFoodRepository {

    private val client = RetrofitOpenApiFoodInstance.getInstance().create(OpenApiFoodService::class.java)

    suspend fun getFoodList(foodName : String, pageNo : Int)
        = client.getFoodList(BuildConfig.OPEN_API_FOOD_KEY, foodName, pageNo)
}