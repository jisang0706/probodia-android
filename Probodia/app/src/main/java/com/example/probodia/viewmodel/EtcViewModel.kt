package com.example.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.model.UserDto
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class EtcViewModel : ViewModel() {

    val serverRepository = ServerRepository()

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

    fun getUserProfile(preferenceRepository : PreferenceRepository, userId : String) = viewModelScope.launch {
        val accessToken = preferenceRepository.getApiToken().apiAccessToken
        _userData.value = serverRepository.getUserData(accessToken, userId)
    }
}