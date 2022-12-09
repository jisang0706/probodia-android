package com.piri.probodia.data.remote.body

data class FoodAllGLBody(
    val foodList : MutableList<FoodGL>
) {
    data class FoodGL(
        val foodId : String,
        val quantity : Int
    )
}
