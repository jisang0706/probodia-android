package com.example.probodia.data.remote.api

import com.example.probodia.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAIServerInstance {

    private val client = Retrofit
        .Builder()
        .baseUrl(BuildConfig.AI_SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance() : Retrofit {
        return client
    }
}