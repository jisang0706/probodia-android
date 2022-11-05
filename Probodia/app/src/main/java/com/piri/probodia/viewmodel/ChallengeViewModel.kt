package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.ChallengeDto
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerChallengeRepository
import kotlinx.coroutines.launch

class ChallengeViewModel : BaseViewModel() {

    val serverChallengeRepository = ServerChallengeRepository()

    val _result = MutableLiveData<MutableList<ChallengeDto>>()
    val result : LiveData<MutableList<ChallengeDto>>
        get() = _result

    fun getChallengeList(preferenceRepository : PreferenceRepository) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _result.value = serverChallengeRepository.getChallengeList(accessToken)
        } catch (e : Exception) {
            Log.e("CHALLENGE", e.message.toString())
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _result.value = serverChallengeRepository.getChallengeList(accessToken)
        }
    }
}