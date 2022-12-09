package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerChallengeRepository
import kotlinx.coroutines.launch

class ChallengeViewViewModel : BaseViewModel() {

    val serverChallengeRepo = ServerChallengeRepository()

    val _challengeResult = MutableLiveData<MutableList<ChallengeDto>>()
    val challengeResult : LiveData<MutableList<ChallengeDto>>
        get() = _challengeResult

    fun getChallengeList(preferenceRepo : PreferenceRepository) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _challengeResult.value = serverChallengeRepo.getChallengeList(accessToken)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _challengeResult.value = serverChallengeRepo.getChallengeList(accessToken)
        }
    }
}