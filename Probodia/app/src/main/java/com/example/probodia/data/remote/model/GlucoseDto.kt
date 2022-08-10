package com.example.probodia.data.remote.model

import com.google.gson.annotations.SerializedName

data class GlucoseDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {
    data class Record(
        val timeTag : String,

        @SerializedName("bloodSugar")
        val glucose : Int,

        val recordId : Int,

        val recordDate : String
    )
}
