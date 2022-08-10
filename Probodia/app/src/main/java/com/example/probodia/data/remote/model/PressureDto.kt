package com.example.probodia.data.remote.model

import com.google.gson.annotations.SerializedName

data class PressureDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {
    data class Record(
        val timeTag : String,

        @SerializedName("maxBloodPressure")
        val maxPressure : Int,

        @SerializedName("minBloodPressure")
        val minPressure : Int,

        @SerializedName("heartBeat")
        val heartRate : Int,

        val recordId : Int,

        val recordDate : String
    )
}
