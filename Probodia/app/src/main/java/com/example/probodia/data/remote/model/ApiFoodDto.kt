package com.example.probodia.data.remote.model

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
        )
    }
}
