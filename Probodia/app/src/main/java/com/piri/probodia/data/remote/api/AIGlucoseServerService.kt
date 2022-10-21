package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.model.FoodDetailDto
import com.piri.probodia.data.remote.model.GLDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AIGlucoseServerService {

    @POST("GL")
    suspend fun getGL(
        @Header("Authorization") token : String,
        @Body getGLBody : FoodDetailDto
    ) : GLDto
}