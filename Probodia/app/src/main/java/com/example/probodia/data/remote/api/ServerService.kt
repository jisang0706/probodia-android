package com.example.probodia.data.remote.api

import com.example.probodia.data.remote.body.GetApiTokenBody
import com.example.probodia.data.remote.model.ApiToken
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ServerService {

    @POST("user-service/login")
    suspend fun getApiToken(
        @Body getApiTokenBody: GetApiTokenBody
    ) : ApiToken
}