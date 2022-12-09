package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.body.*
import com.piri.probodia.data.remote.model.*
import retrofit2.http.*

interface ServerService {

    @POST("login")
    suspend fun getApiToken(
        @Body getApiTokenBody : GetApiTokenBody
    ) : ApiToken

    @GET("isCreated")
    suspend fun getUserJoined(
        @Header("userId") userId : Long
    ) : Boolean

    @GET("auth/refresh")
    suspend fun refreshApiToken(
        @Header("Authorization") token : String,
        @Header("RefreshToken") refreshToken : String
    ) : String

    @GET("api/version/{userVersion}")
    suspend fun getVersionRunnable(
        @Header("Authorization") token : String,
        @Path("userVersion") version : Int
    ) : Boolean

    @POST("api/record/sugar")
    suspend fun postGlucose(
        @Header("Authorization") token : String,
        @Body postGlucoseBody : PostGlucoseBody
    ) : GlucoseDto.Record

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
    ) : PressureDto.Record

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
    ) : MedicineDto.Record

    @DELETE("api/record/medicine/{recordId}")
    suspend fun deleteMedicine(
        @Header("Authorization") token : String,
        @Path("recordId") recordId : Int
    ) : Int

    @POST("api/record/medicine/update")
    suspend fun putMedicine(
        @Header("Authorization") token : String,
        @Body putMedicine : PutMedicineBody
    ) : MedicineDto.Record

    @POST("api/record/meal")
    suspend fun postMeal(
        @Header("Authorization") token : String,
        @Body postMealBody : PostMealBody
    ) : MealDto.Record

    @DELETE("api/record/meal/{recordId}")
    suspend fun deleteMeal(
        @Header("Authorization") token : String,
        @Path("recordId") recordId : Int
    ) : Int

    @POST("api/record/meal/update")
    suspend fun putMeal(
        @Header("Authorization") token : String,
        @Body putMeal : PutMealBody
    ) : MealDto.Record

    @POST("api/record/getAllByDateAndTimeTag")
    suspend fun getRecord(
        @Header("Authorization") token : String,
        @Body getRecordBody : GetRecordBody
    ) : MutableList<TodayRecord.AllData>

    @GET("api/users")
    suspend fun getUserData(
        @Header("Authorization") token : String,
        @Header("userId") userId : String
    ) : UserDto

    @PUT("api/users")
    suspend fun putUserData(
        @Header("Authorization") token : String,
        @Body userData : PutUserData
    ) : UserDto

    @GET("api/recordStat/average-nutrient/{start}/{end}")
    suspend fun getNutrient(
        @Header("Authorization") token : String,
        @Path("start") startDate : String,
        @Path("end") endDate : String
    ) : NutrientDto

    @GET("api/recordStat/hemoglobin/{start}/{end}")
    suspend fun getHemoglobin(
        @Header("Authorization") token : String,
        @Path("start") startDate : String,
        @Path("end") endDate : String
    ) : Double
}