package com.piri.probodia.data.remote.api

import android.content.Context
import com.piri.probodia.BuildConfig
import com.google.gson.GsonBuilder
import com.piri.probodia.widget.utils.SSLUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServerInstance {

    val gson = GsonBuilder().setLenient().create()!!

    private lateinit var client : Retrofit

    fun getInstance() : Retrofit {
        return client
    }

    fun initInstance(context : Context) {
        client = Retrofit
            .Builder()
            .client(
                SSLUtil.generateSecureOkHttpClient(context, 1)
            )
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}