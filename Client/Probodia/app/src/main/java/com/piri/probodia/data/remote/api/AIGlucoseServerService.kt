package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.body.FoodAllGLBody
import com.piri.probodia.data.remote.body.FoodGLBody
import com.piri.probodia.data.remote.model.AllGLDto
import com.piri.probodia.data.remote.model.OneGLDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AIGlucoseServerService {

    @POST("oneGL")
    suspend fun getGL(
        @Header("Authorization") token : String,
        @Body getGLBody : FoodGLBody
    ) : OneGLDto

    @POST("allGL")
    suspend fun getAllGL(
        @Header("Authorization") token : String,
        @Body getAllGLBody: FoodAllGLBody
    ) : AllGLDto
}