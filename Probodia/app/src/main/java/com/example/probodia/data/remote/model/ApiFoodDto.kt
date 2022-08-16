package com.example.probodia.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ApiFoodDto(
    @SerializedName("I2790")
    val body : Body
) {
    data class Body(
        @SerializedName("total_count")
        val totalCount : String,

        @SerializedName("row")
        val items : MutableList<FoodItem>
    ) {
        data class FoodItem(
            @SerializedName("DESC_KOR")
            val name : String,

            @SerializedName("FOOD_CD")
            val id : String,

            @SerializedName("SERVING_SIZE")
            val quantity : String,

            @SerializedName("NUTR_CONT1")
            val kcal : String,

            @SerializedName("NUTR_CONT2")
            val carbohydrate : String,

            @SerializedName("NUTR_CONT3")
            val protein : String,

            @SerializedName("NUTR_CONT4")
            val fat : String,

            @SerializedName("NUTR_CONT5")
            val sugar : String,

            @SerializedName("NUTR_CONT6")
            val sodium : String,

            @SerializedName("NUTR_CONT7")
            val cholesterol : String,

            @SerializedName("NUTR_CONT8")
            val saturated_fatty_acid : String,

            @SerializedName("NUTR_CONT9")
            val trans_fat : String
        ) : Parcelable {

            constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!
            )

            override fun describeContents() = 0

            override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest?.writeString(name)
                dest?.writeString(id)
                dest?.writeString(quantity)
                dest?.writeString(kcal)
                dest?.writeString(carbohydrate)
                dest?.writeString(protein)
                dest?.writeString(fat)
                dest?.writeString(sugar)
                dest?.writeString(sodium)
                dest?.writeString(cholesterol)
                dest?.writeString(saturated_fatty_acid)
                dest?.writeString(trans_fat)
            }

            companion object CREATOR : Parcelable.Creator<FoodItem> {
                override fun createFromParcel(parcel: Parcel): FoodItem {
                    return FoodItem(parcel)
                }

                override fun newArray(size: Int): Array<FoodItem?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
