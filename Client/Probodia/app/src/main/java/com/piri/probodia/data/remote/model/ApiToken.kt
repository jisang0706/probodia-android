package com.piri.probodia.data.remote.model

data class ApiToken(
    var apiAccessToken: String,
    val apiRefreshToken: String,
    val isSignUp : Boolean
)