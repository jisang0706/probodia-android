package com.example.probodia.data.remote.api

import com.example.probodia.data.remote.body.*
import com.example.probodia.data.remote.model.*
import retrofit2.http.*

interface ServerService {

    @POST("login")
    suspend fun getApiToken(
        @Body getApiTokenBody : GetApiTokenBody
    ) : ApiToken

    @POST("api/record/sugar")
    suspend fun postGlucose(
        @Header("Authorization") token : String,
        @Body postGlucoseBody : PostGlucoseBody
    ) : GlucoseDto

    @POST("api/record/pressure")
    suspend fun postPressure(
        @Header("Authorization") token : String,
        @Body postPressureBody : PostPressureBody
    ) : PressureDto

    @POST("api/record/medicine")
    suspend fun postMedicine(
        @Header("Authorization") token : String,
        @Body postMedicineBody : PostMedicineBody
    ) : MedicineDto

    @POST("api/record/meal")
    suspend fun postMeal(
        @Header("Authorization") token : String,
        @Body postMealBody : PostMealBody
    ) : MealDto

    @POST("api/record/getAllByDateAndTimeTag")
    suspend fun getRecord(
        @Header("Authorization") token : String,
        @Body getRecordBody : GetRecordBody
    ) : MutableList<TodayRecord.AllData>
}