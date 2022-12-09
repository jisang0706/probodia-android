package com.piri.probodia.repository

import com.piri.probodia.data.remote.api.RetrofitServerInstance
import com.piri.probodia.data.remote.api.ServerService
import com.piri.probodia.data.remote.body.*
import com.piri.probodia.data.remote.model.*

class ServerRepository {

    private val client = RetrofitServerInstance.getInstance().create(ServerService::class.java)

    suspend fun getApiToken(userId : Long, accessToken: String)
        = client.getApiToken(GetApiTokenBody(userId, accessToken))

    suspend fun getUserJoined(userId : Long) =
        client.getUserJoined(userId)

    suspend fun refreshApiToken(apiToken : String, refreshApiToken : String) =
        client.refreshApiToken("Bearer $apiToken", refreshApiToken)

    suspend fun getVersionRunnable(apiToken : String, version : Int) =
        client.getVersionRunnable("Bearer $apiToken", version)

    suspend fun postGlucose(apiToken : String, timeTag : String, glucose : Int, recordDate : String)
        = client.postGlucose("Bearer $apiToken", PostGlucoseBody(timeTag, glucose, recordDate))

    suspend fun deleteGlucose(apiToken : String, recordId : Int)
        = client.deleteGlucose("Bearer $apiToken", recordId)

    suspend fun putGlucose(apiToken : String, recordId : Int, timeTag : String, glucose : Int, recordDate : String)
        = client.putGlucose("Bearer $apiToken", PutGlucoseBody(recordId, timeTag, glucose, recordDate))

    suspend fun postPressure(apiToken : String, timeTag: String, maxPressure : Int, minPressure : Int, heartRate : Int, recordDate : String)
        = client.postPressure("Bearer $apiToken", PostPressureBody(timeTag, maxPressure, minPressure, heartRate, recordDate))

    suspend fun deletePressure(apiToken : String, recordId : Int)
            = client.deletePressure("Bearer $apiToken", recordId)

    suspend fun putPressure(apiToken : String, recordId : Int, timeTag : String, maxPressure : Int, minPressure : Int, heartRate : Int, recordDate : String)
            = client.putPressure("Bearer $apiToken", PutPressureBody(recordId, timeTag, maxPressure, minPressure, heartRate, recordDate))

    suspend fun postMedicine(apiToken : String, timeTag : String, medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>, recordDate : String) : MedicineDto.Record {
        val itemList : List<PostMedicineBody.PostMedicineItem> = buildList {
            for (medicine in medicineList) {
                add(
                    PostMedicineBody.PostMedicineItem(
                        medicine.item_seq,
                        medicine.itemName,
                        medicine.unit
                    )
                )
            }
        }

        return client.postMedicine("Bearer $apiToken", PostMedicineBody(timeTag, recordDate, itemList))
    }

    suspend fun deleteMedicine(apiToken : String, recordId : Int)
            = client.deleteMedicine("Bearer $apiToken", recordId)

    suspend fun putMedicine(apiToken : String, recordId : Int, timeTag : String, recordDate : String, medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>) : MedicineDto.Record {
        val itemList : List<PutMedicineBody.MedicineDetail> = buildList {
            for (medicine in medicineList) {
                add(
                    PutMedicineBody.MedicineDetail(
                        medicine.unit,
                        medicine.itemName,
                        medicine.item_seq
                    )
                )
            }
        }

        return client.putMedicine("Bearer $apiToken", PutMedicineBody(recordId, timeTag, recordDate, itemList))
    }

    suspend fun postMeal(apiToken : String, timeTag : String, foodList : MutableList<PostMealBody.PostMealItem>, recordDate : String) : MealDto.Record {
        return client.postMeal("Bearer $apiToken", PostMealBody(timeTag, recordDate, foodList))
    }

    suspend fun deleteMeal(apiToken : String, recordId : Int)
            = client.deleteMeal("Bearer $apiToken", recordId)

    suspend fun putMeal(apiToken : String, recordId : Int, timeTag : String, recordDate : String, foodList : MutableList<PostMealBody.PostMealItem>) : MealDto.Record {
        return client.putMeal("Bearer $apiToken", PutMealBody(recordId, timeTag, recordDate, foodList))
    }

    suspend fun getRecords(apiToken: String, getRecordBody: GetRecordBody) : MutableList<TodayRecord.AllData>
         = client.getRecord("Bearer $apiToken", getRecordBody)

    suspend fun getUserData(apiToken : String, userId : String)
        = client.getUserData("Bearer $apiToken", userId)

    suspend fun putUserData(apiToken : String, userData : PutUserData)
        = client.putUserData("Bearer $apiToken", userData)

    suspend fun getNutrient(apiToken : String, startDate : String, endDate : String)
        = client.getNutrient("Bearer $apiToken", startDate, endDate)

    suspend fun getHemoglobin(apiToken : String, startDate : String, endDate : String)
            = client.getHemoglobin("Bearer $apiToken", startDate, endDate)
}