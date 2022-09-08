package com.example.probodia.data.remote.body

data class PutMealBody(
    var recordId : Int,
    var timeTag : String,
    var recordDate : String,
    var mealDetails : List<MealDetail>
) {
    data class MealDetail (
        var mealDetailId : Int,
        var calories : Int,
        var imageUrl : String,
        var bloodSugar : Int,
        var foodName : String,
        var quantity : Int
            )
}
