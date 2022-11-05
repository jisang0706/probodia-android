package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.model.ChallengeDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ServerChallengeService {

    @GET("api/challenge/getAll")
    suspend fun getChallengeList(
        @Header("Authorization") token : String
    ) : MutableList<ChallengeDto>
}