package com.piri.probodia.data.remote.model

data class OneGLDto (
    val main : GLDto,
    val foodBig : GLDto,
    val foodSmall : GLDto
        ) {
    data class GLDto(
        val foodId : String,
        val foodGL : Double,
        val healthGL : String
    )
}


