package com.piri.probodia.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MealDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {

    @Parcelize
    data class Record(
        val timeTag : String,

        val mealDetails : List<MealDetail>,

        val recordId : Int,

        val recordDate : String,
    ) : Parcelable

    @Parcelize
    data class MealDetail(
        val mealDetailId : Int,

        val calories : Int,

        val imageUrl : String,

        val bloodSugar : Int,

        val foodName : String,

        val quantity : Int,

        val foodId : String
    ) : Parcelable
}