package com.example.probodia.data.remote.api

import com.example.probodia.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitOpenApiFoodInstance {

    val gson = GsonBuilder().setLenient().create()

    private val client = Retrofit
        .Builder()
        .baseUrl(BuildConfig.OPEN_API_FOOD_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getInstance() : Retrofit {
        return client
    }
}