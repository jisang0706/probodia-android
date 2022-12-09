package com.piri.probodia.data.remote.model

data class SortationDto(
    override val type: String,

    val record : Record
) : RecordDatasBase {
    data class Record(
        val timeTag : String,

        val recordDate : String
    )
}
