package com.piri.probodia.data.remote.body

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PostMealBody (
    var timeTag : String,
    var recordDate : String,
    var mealDetails : List<PostMealItem>
        ) {

    @Parcelize
    data class PostMealItem (
        var foodName : String,
        var foodId : String,
        var quantity : Double,
        var calories : Int,
        var bloodSugar : Int,
        var imageUrl : String
            ) : Parcelable {
    }
}