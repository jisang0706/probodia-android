package com.example.probodia.data.remote.model

data class UserDto(
    val username : String,
    val profileImageUrl : String,
    val diabeteCode : String,
    val age : Int,
    val sex : String,
    val height : Int,
    val weight : Int
)
