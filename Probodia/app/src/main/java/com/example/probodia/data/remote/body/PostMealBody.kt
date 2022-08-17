package com.example.probodia.data.remote.body

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
            )
}