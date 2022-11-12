package com.piri.probodia.repository

import com.piri.probodia.data.remote.api.RetrofitServerChallengeInstance
import com.piri.probodia.data.remote.api.ServerChallengeService

class ServerChallengeRepository {

    private val client = RetrofitServerChallengeInstance.getInstance().create(ServerChallengeService::class.java)

    suspend fun getChallengeList (apiToken : String)
        = client.getChallengeList("Bearer $apiToken")

    suspend fun participateChallenge(apiToken : String, challengeId : Int)
        = client.participateChallenge("Bearer $apiToken", challengeId)

    suspend fun getParticipatedChallenge(apiToken : String)
        = client.getParticipatedChallenge("Bearer $apiToken")

    suspend fun getChallengeRecord(apiToken : String, challengeId : Int)
        = client.getChallengeRecord("Bearer $apiToken", challengeId)
}