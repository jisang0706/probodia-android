package com.example.probodia.data.remote.model

data class MealDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {
    data class Record(
        val timeTag : String,

        val mealDetails : List<MealDetail>,

        val recordId : Int,

        val recordDate : String
    )

    data class MealDetail(
        val mealDetailId : Int,

        val calories : Int,

        val imageUrl : String,

        val bloodSugar : Int,

        val foodName : String
    )
}