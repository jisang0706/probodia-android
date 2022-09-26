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

    private var _kakaoLoginMutableLiveData = SingleLiveEvent<Boolean>()
    val liveKakaoLogin : LiveData<Boolean>
        get() = _kakaoLoginMutableLiveData

    private var _kakaoUserId = MutableLiveData(0.toLong())
    val liveKakaoUserId : LiveData<Long>
        get() = _kakaoUserId

    private var _kakaoAccessToken = MutableLiveData("")
    val liveKakaoAccessToken : LiveData<String>
        get() = _kakaoAccessToken

    private var _buttonLogin = SingleLiveEvent<Boolean>()
    val buttonLogin : LiveData<Boolean>
        get() = _buttonLogin

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
        _kakaoLoginMutableLiveData.call()
    }

    fun setKakaoUserId(userId: Long) {
        _kakaoUserId.value = userId
    }

    fun setKakaoAccessToken(accessToken: String) {
        _kakaoAccessToken.value = accessToken
    }

    suspend fun getApiToken() : ApiToken
        =  serverRepository.getApiToken(liveKakaoUserId.value!!, liveKakaoAccessToken.value!!)

    suspend fun getUserJoined() : Boolean =
        serverRepository.getUserJoined(liveKakaoUserId.value!!)

    fun saveApiToken(preferenceRepository : PreferenceRepository, apiToken: ApiToken) = viewModelScope.launch {
        preferenceRepository.saveApiToken(apiToken)
    }
}