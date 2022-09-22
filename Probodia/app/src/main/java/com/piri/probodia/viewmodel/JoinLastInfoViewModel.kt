package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.body.PutUserData
import com.piri.probodia.data.remote.model.ApiToken
import com.piri.probodia.data.remote.model.UserDto
import com.piri.probodia.repository.PreferenceRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class JoinLastInfoViewModel : JoinButtonSelectViewModel() {

    private val _joinResult = MutableLiveData<UserDto>()
    val joinResult : LiveData<UserDto>
        get() = _joinResult

    fun putUserData(preferenceRepository : PreferenceRepository, userId : String, age : Int, gender : String, height : Int, weight : Int, diabete : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            val userData = PutUserData(
                userId, height, weight, gender, diabete, age)
            _joinResult.value = serverRepository.putUserData(accessToken, userData)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _joinResult.value = serverRepository.putUserData(accessToken, PutUserData(
                userId, height, weight, gender, diabete, age
            ))
        }
    }

    suspend fun getApiToken(userId : Long, kakaoAccessToken : String) =
        serverRepository.getApiToken(userId, kakaoAccessToken)

    fun saveApiToken(preferenceRepository : PreferenceRepository, apiToken : ApiToken) = viewModelScope.launch {
        preferenceRepository.saveApiToken(apiToken)
    }
}