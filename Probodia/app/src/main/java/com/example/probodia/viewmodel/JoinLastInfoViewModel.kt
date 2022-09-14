package com.example.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.probodia.data.remote.body.PutUserData
import com.example.probodia.data.remote.model.UserDto
import com.example.probodia.repository.PreferenceRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class JoinLastInfoViewModel : JoinButtonSelectViewModel() {

    private val _joinResult = MutableLiveData<UserDto>()
    val joinResult : LiveData<UserDto>
        get() = _joinResult

    private val _userId = MutableLiveData<String>()
    val userId : LiveData<String>
        get() = _userId

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

    fun getUserId() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {

            } else if (tokenInfo != null) {
                _userId.value = "${tokenInfo.id}"
            }
        }
    }
}