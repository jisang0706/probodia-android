package com.example.probodia.data.remote.model

import com.google.gson.annotations.SerializedName

data class FoodNamesDto (
    @SerializedName("class_id")
    val foodList : List<String>
        )