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
            Log.e("ACCESSTOKEN", _apiToken.value.toString())
            _join.value = false
        } else {
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
}