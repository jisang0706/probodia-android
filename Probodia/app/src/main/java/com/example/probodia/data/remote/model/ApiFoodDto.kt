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
            override var itemName : String,

            @SerializedName("FOOD_CD")
            val id : String,

            @SerializedName("SERVING_SIZE")
            var quantity : String,

            @SerializedName("NUTR_CONT1")
            var kcal : String,

            @SerializedName("NUTR_CONT2")
            var carbohydrate : String,

            @SerializedName("NUTR_CONT3")
            var protein : String,

            @SerializedName("NUTR_CONT4")
            var fat : String,

            @SerializedName("NUTR_CONT5")
            var sugar : String,

            @SerializedName("NUTR_CONT6")
            var sodium : String,

            @SerializedName("NUTR_CONT7")
            var cholesterol : String,

            @SerializedName("NUTR_CONT8")
            var saturated_fatty_acid : String,

            @SerializedName("NUTR_CONT9")
            var trans_fat : String
        ) : Parcelable, ApiItemName {

            var eat_quantity : Int = quantity.toInt()
            var glucose : Int = 0
            var image_url : String = ""

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
                dest?.writeString(itemName)
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
