package com.example.probodia.data.remote.body

data class PutGlucoseBody(
    var recordId : Int,
    var timeTag : String,
    var bloodSugar : Int,
    var recordDate : String
)