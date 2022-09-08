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

    @DELETE("api/record/sugar/{recordId}")
    suspend fun deleteGlucose(
        @Header("Authorization") token : String,
        @Path("recordId") recordId : Int
    ) : Int

    @PUT("api/record/sugar")
    suspend fun putGlucose(
        @Header("Authorization") token : String,
        @Body putGlucose : PutGlucoseBody
    ) : GlucoseDto.Record

    @POST("api/record/pressure")
    suspend fun postPressure(
        @Header("Authorization") token : String,
        @Body postPressureBody : PostPressureBody
    ) : PressureDto

    @DELETE("api/record/pressure/{recordId}")
    suspend fun deletePressure(
        @Header("Authorization") token : String,
        @Path("recordId") recordId : Int
    ) : Int

    @PUT("api/record/pressure")
    suspend fun putPressure(
        @Header("Authorization") token : String,
        @Body putPressure : PutPressureBody
    ) : PressureDto.Record

    @POST("api/record/medicine")
    suspend fun postMedicine(
        @Header("Authorization") token : String,
        @Body postMedicineBody : PostMedicineBody
    ) : MedicineDto

    @DELETE("api/record/medicine/{recordId}")
    suspend fun deleteMedicine(
        @Header("Authorization") token : String,
        @Path("recordId") recordId : Int
    ) : Int

    @PUT("api/record/medicine")
    suspend fun putMedicine(
        @Header("Authorization") token : String,
        @Body putMedicine : PutMedicineBody
    ) : MedicineDto.Record

    @POST("api/record/meal")
    suspend fun postMeal(
        @Header("Authorization") token : String,
        @Body postMealBody : PostMealBody
    ) : MealDto

    @DELETE("api/record/meal/{recordId}")
    suspend fun deleteMeal(
        @Header("Authorization") token : String,
        @Path("recordId") recordId : Int
    ) : Int

    @PUT("api/record/meal")
    suspend fun putMeal(
        @Header("Authorization") token : String,
        @Body putMeal : PutMealBody
    ) : MealDto.Record

    @POST("api/record/getAllByDateAndTimeTag")
    suspend fun getRecord(
        @Header("Authorization") token : String,
        @Body getRecordBody : GetRecordBody
    ) : MutableList<TodayRecord.AllData>
}