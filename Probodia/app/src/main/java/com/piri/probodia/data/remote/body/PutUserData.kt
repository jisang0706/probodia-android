package com.piri.probodia.data.remote.body

data class PutUserData(
    val userId : String,
    val height : Int,
    val weight : Int,
    val sex : String,
    val diabeteCode : String,
    val age : Int
)