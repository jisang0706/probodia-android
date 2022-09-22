package com.piri.probodia.data.remote.model

import com.google.gson.annotations.SerializedName

data class FoodDto(
    val data : List<FoodItem>
) {
    data class FoodItem(

        @SerializedName("foodName")
        override val itemName: String,

        val foodId : String,
    ) : ApiItemName
}
