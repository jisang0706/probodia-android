package com.piri.probodia.data.remote.api

import com.piri.probodia.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServerFoodInstance {

    private val client = Retrofit
        .Builder()
        .baseUrl(BuildConfig.SERVER_FOOD_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance() : Retrofit = client
}