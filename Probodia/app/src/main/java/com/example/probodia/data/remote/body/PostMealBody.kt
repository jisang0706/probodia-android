package com.example.probodia.data.remote.body

import android.os.Parcel
import android.os.Parcelable

data class PostMealBody (
    var timeTag : String,
    var recordDate : String,
    var mealDetails : List<PostMealItem>
        ) {
    data class PostMealItem (
        var foodName : String,
        var foodId : String,
        var quantity : Int,
        var calories : Int,
        var bloodSugar : Int,
        var imageUrl : String
            ) : Parcelable {

        constructor(parcel : Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt()!!,
            parcel.readInt()!!,
            parcel.readInt()!!,
            parcel.readString()!!,
        )

        override fun describeContents(): Int  = 0

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeString(foodName)
            dest?.writeString(foodId)
            dest?.writeInt(quantity)
            dest?.writeInt(calories)
            dest?.writeInt(bloodSugar)
            dest?.writeString(imageUrl)
        }

        companion object CREATOR : Parcelable.Creator<PostMealItem> {
            override fun createFromParcel(source: Parcel): PostMealItem {
                return PostMealItem(source)
            }

            override fun newArray(size: Int): Array<PostMealItem?> {
                return arrayOfNulls(size)
            }

        }
    }
}