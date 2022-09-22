package com.piri.probodia.data.remote.body

data class PostPressureBody(
    var timeTag : String,
    var maxBloodPressure : Int,
    var minBloodPressure : Int,
    var heartBeat : Int,
    var recordDate : String
)
