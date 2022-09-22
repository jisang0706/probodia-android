package com.piri.probodia.repository

import com.piri.probodia.data.remote.api.RetrofitServerFoodInstance
import com.piri.probodia.data.remote.api.ServerFoodService
import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.data.remote.model.FoodDto

class ServerFoodRepository {

    private val client = RetrofitServerFoodInstance.getInstance().create(ServerFoodService::class.java)

    suspend fun getFoodList(apiToken : String, foodName : String, pageNo : Int) : FoodDto
        = client.getFoodList("Bearer $apiToken", foodName, pageNo, 20)

    suspend fun getFoodDetail(apiToken : String, foodId : String) : FoodDetailDto
        = client.getFoodDetail("Bearer $apiToken", foodId)
}