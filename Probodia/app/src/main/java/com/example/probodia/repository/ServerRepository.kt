package com.example.probodia.repository

import android.util.Log
import com.example.probodia.data.remote.api.RetrofitServerInstance
import com.example.probodia.data.remote.api.ServerService
import com.example.probodia.data.remote.body.*
import com.example.probodia.data.remote.model.*

class ServerRepository {

    private val client = RetrofitServerInstance.getInstance().create(ServerService::class.java)

    suspend fun getApiToken(userId : Long, accessToken: String)
        = client.getApiToken(GetApiTokenBody(userId, accessToken))

//    suspend fun getTodayRecords(apiToken : String, page : Int, pageSize : Int)
//        = client.getTodayRecord("Bearer ${apiToken}", page, pageSize)

    suspend fun postGlucose(apiToken : String, timeTag : String, glucose : Int, recordDate : String)
        = client.postGlucose("Bearer ${apiToken}", PostGlucoseBody(timeTag, glucose, recordDate))

    suspend fun postPressure(apiToken : String, timeTag: String, maxPressure : Int, minPressure : Int, heartRate : Int, recordDate : String)
        = client.postPressure("Bearer ${apiToken}", PostPressureBody(timeTag, maxPressure, minPressure, heartRate, recordDate))

    suspend fun postMedicine(apiToken : String, timeTag : String, medicineList : MutableList<ApiMedicineDto.Body.MedicineItem>, recordDate : String) : MedicineDto {
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

        return client.postMedicine("Bearer ${apiToken}", PostMedicineBody(timeTag, recordDate, itemList))
    }

    suspend fun postMeal(apiToken : String, timeTag : String, foodList : MutableList<ApiFoodDto.Body.FoodItem>, recordDate : String) : MealDto {
        val itemList : List<PostMealBody.PostMealItem> = buildList {
            for(food in foodList) {
                add(
                    PostMealBody.PostMealItem(
                        food.itemName,
                        food.id,
                        food.eat_quantity,
                        food.kcal.toDouble().toInt(),
                        food.glucose,
                        food.image_url
                    )
                )
            }
        }
        Log.e("POSTMEALLIST", itemList.toString())

        return client.postMeal("Bearer ${apiToken}", PostMealBody(timeTag, recordDate, itemList))
    }

    suspend fun getRecords(apiToken: String, getRecordBody: GetRecordBody) : MutableList<TodayRecord.AllData>
         = client.getRecord("Bearer ${apiToken}", getRecordBody)
}