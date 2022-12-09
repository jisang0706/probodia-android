package com.piri.probodia.data.remote.body

data class GetApiTokenBody(
    var id : Long,
    var oauthAccessToken : String
)
