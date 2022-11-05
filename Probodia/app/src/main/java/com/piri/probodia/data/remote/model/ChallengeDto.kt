package com.piri.probodia.data.remote.model

data class ChallengeDto (
    val id : Int,
    val type : String,
    val official : Boolean,
    val name : String,
    val content : String,
    val earnPoint : Int,
    val totalCnt : Int,
    val stDate : String,
    val edDate : String,
    val frequency : Frequency,
    val caution : MutableList<String>,
) {
    data class Frequency (
        val dataType : String,
        val period : Int,
        val time : Int
            )
}
