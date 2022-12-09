package com.piri.probodia.data.remote.api

import android.content.Context
import com.piri.probodia.BuildConfig
import com.piri.probodia.widget.utils.SSLUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServerFoodInstance {

    private lateinit var client : Retrofit

    fun getInstance() : Retrofit = client

    fun initInstance(context : Context) {
        client = Retrofit
            .Builder()
            .client(
                SSLUtil.generateSecureOkHttpClient(context, 1)
            )
            .baseUrl(BuildConfig.SERVER_FOOD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}