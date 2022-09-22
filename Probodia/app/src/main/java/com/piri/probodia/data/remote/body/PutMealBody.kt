package com.piri.probodia.data.remote.body

data class PutMealBody(
    var recordId : Int,
    var timeTag : String,
    var recordDate : String,
    var mealDetails : List<PostMealBody.PostMealItem>
) {
}
