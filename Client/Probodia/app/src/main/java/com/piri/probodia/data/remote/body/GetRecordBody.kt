package com.piri.probodia.data.remote.body

data class GetRecordBody(
    val startDate : String,
    val endDate : String,
    val filterType : MutableList<String>,
    val timeTagList : MutableList<String>
)
