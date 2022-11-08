package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerChallengeRepository
import kotlinx.coroutines.launch

class ChallengeParticipatingViewModel : BaseViewModel() {

    private val serverChallengeRepo = ServerChallengeRepository()

    private val _challengeResult = MutableLiveData<MutableList<ChallengeDto>>()
    val challengeResult : LiveData<MutableList<ChallengeDto>>
        get() = _challengeResult

    fun getParticipatingChallengeList(preferenceRepo : PreferenceRepository) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _challengeResult.value = serverChallengeRepo.getParticipatedChallenge(accessToken)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _challengeResult.value = serverChallengeRepo.getParticipatedChallenge(accessToken)
        }
    }
}