package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.data.remote.model.ApiToken
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.widget.utils.SingleLiveEvent
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.piri.probodia.data.remote.body.PutUserData
import com.piri.probodia.data.remote.model.UserDto
import kotlinx.coroutines.launch

class IntroViewModel : BaseViewModel() {

    private var _kakaoLogin = SingleLiveEvent<Boolean>()
    val kakaoLogin : LiveData<Boolean>
        get() = _kakaoLogin

    private var _kakaoUserId = MutableLiveData(0.toLong())
    val kakaoUserId : LiveData<Long>
        get() = _kakaoUserId

    private var _kakaoAccessToken = MutableLiveData("")
    val kakaoAccessToken : LiveData<String>
        get() = _kakaoAccessToken

    private var _buttonLogin = SingleLiveEvent<Boolean>()
    val buttonLogin : LiveData<Boolean>
        get() = _buttonLogin

    private var _join = MutableLiveData<Boolean>()
    val join : LiveData<Boolean>
        get() = _join

    private var _apiToken = MutableLiveData<ApiToken>()
    val apiToken : LiveData<ApiToken>
        get() = _apiToken

    private val _joinResult = MutableLiveData<UserDto>()
    val joinResult : LiveData<UserDto>
        get() = _joinResult

    fun autoLogin() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        // 로그인 필요
                    } else {
                        // 기타 에러
                    }
                    _buttonLogin.call()
                } else if (tokenInfo != null) {
                    setKakaoUserId(tokenInfo.id!!)
                }
            }
        } else {
            _buttonLogin.call()
        }
    }

    fun kakaoLogin() {
        _kakaoLogin.call()
    }

    fun setKakaoUserId(userId: Long) {
        _kakaoUserId.value = userId
    }

    fun setKakaoAccessToken(accessToken: String) {
        _kakaoAccessToken.value = accessToken
    }

    fun apiLogin() = viewModelScope.launch(coroutineExceptionHandler) {
        if (getUserJoined()) {
            _apiToken.value = getApiToken()
            _join.value = false
        } else {
            _apiToken.value = getApiToken()
            _join.value = true
        }
    }

    suspend fun getApiToken() : ApiToken
        =  serverRepository.getApiToken(kakaoUserId.value!!, kakaoAccessToken.value!!)

    suspend fun getUserJoined() : Boolean =
        serverRepository.getUserJoined(kakaoUserId.value!!)

    fun saveApiToken(preferenceRepository : PreferenceRepository, apiToken: ApiToken) = viewModelScope.launch {
        preferenceRepository.saveApiToken(apiToken)
    }

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
}