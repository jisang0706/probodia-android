package com.piri.probodia.viewmodel

import androidx.lifecycle.*
import com.piri.probodia.data.remote.model.UserDto
import com.piri.probodia.repository.PreferenceRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class EtcViewModel : BaseViewModel() {

    val _userId = MutableLiveData<String>()
    val userId : LiveData<String>
        get() = _userId

    val _userData = MutableLiveData<UserDto>()
    val userData : LiveData<UserDto>
        get() = _userData

    fun getUserId() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {

            } else if (tokenInfo != null) {
                _userId.value = "${tokenInfo.id}"
            }
        }
    }

    fun getUserProfile(preferenceRepository : PreferenceRepository, userId : String) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _getUserProfile("$accessToken", userId)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepository)
            val accessToken = preferenceRepository.getApiToken().apiAccessToken
            _getUserProfile(accessToken, userId)
        }
    }

    suspend fun _getUserProfile(accessToken : String, userId : String) {
        _userData.value = serverRepository.getUserData(accessToken, userId)
    }
}