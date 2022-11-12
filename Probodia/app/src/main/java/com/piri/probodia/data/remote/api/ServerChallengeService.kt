package com.piri.probodia.data.remote.api

import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.data.remote.model.ChallengeParticipateDto
import com.piri.probodia.data.remote.model.ChallengeRecordDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerChallengeService {

    @GET("api/challenge/getAll")
    suspend fun getChallengeList(
        @Header("Authorization") token : String
    ) : MutableList<ChallengeDto>

    @POST("api/challenge/participant/{challengeId}")
    suspend fun participateChallenge(
        @Header("Authorization") token : String,
        @Path("challengeId") challengeId : Int
    ) : ChallengeParticipateDto

    @GET("api/challenge/participant/getJoinChallenge")
    suspend fun getParticipatedChallenge(
        @Header("Authorization") token : String
    ) : MutableList<ChallengeDto>

    @GET("api/challenge/participant/getAllActivity/{challengeId}")
    suspend fun getChallengeRecord(
        @Header("Authorization") token : String,
        @Path("challengeId") challengeId : Int
    ) : MutableList<ChallengeRecordDto>
}