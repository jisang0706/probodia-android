package com.example.probodia.data.remote.model

data class FoodDetailDto(
    val name : String,
    val quantityByOne : Int,
    val quantityByOneUnit : String,
    val calories : Double,
    val carbohydrate : Double,
    val sugars : Double,
    val protein : Double,
    val fat : Double,
    val transFat : Double,
    val saturatedFat : Double,
    val cholesterol : Double,
    val salt : Double
)
