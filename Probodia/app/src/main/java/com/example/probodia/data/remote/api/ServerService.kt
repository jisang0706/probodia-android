package com.example.probodia.data.remote.api

import com.example.probodia.data.remote.body.GetApiTokenBody
import com.example.probodia.data.remote.body.PostGlucoseBody
import com.example.probodia.data.remote.model.ApiToken
import com.example.probodia.data.remote.model.GlucoseDto
import com.example.probodia.data.remote.model.TodayRecord
import retrofit2.http.*

interface ServerService {

    @POST("user-service/login")
    suspend fun getApiToken(
        @Body getApiTokenBody : GetApiTokenBody
    ) : ApiToken

    @GET("user-service/api/record/getAll/{page}/{pageSize}")
    suspend fun getTodayRecord(
        @Header("Authorization") token : String,
        @Path("page") page : Int,
        @Path("pageSize") pageSize : Int
    ) : TodayRecord

    @POST("user-service/api/record/sugar")
    suspend fun postGlucose(
        @Header("Authorization") token : String,
        @Body postGlucoseBody : PostGlucoseBody
    ) : GlucoseDto
}