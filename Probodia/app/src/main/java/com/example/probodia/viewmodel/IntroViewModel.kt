package com.example.probodia.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.probodia.BuildConfig
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient

class IntroViewModel(application: Application) : AndroidViewModel(application) {

    private var _kakaoLoginMutableLiveData = MutableLiveData(false)
    val liveKakaoLogin : LiveData<Boolean>
        get() = _kakaoLoginMutableLiveData

    private val applicationContext = getApplication<Application>().applicationContext

    init {
        KakaoSdk.init(applicationContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    fun autoLogin(goMain: () -> Unit) {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        // 로그인 필요
                    } else {
                        // 기타 에러
                        Log.e("KAKAOLOGIN", "Auto Login : ${error.message}")
                    }
                } else if (tokenInfo != null) {
                    // 로그인 성공
                    Log.e("KAKAOLOGIN", "LOGIN SUCCESS")
                    goMain()
                }
            }
        }
    }

    fun kakaoLogin() {
        _kakaoLoginMutableLiveData.value = !_kakaoLoginMutableLiveData.value!!
    }

    fun setKakaoLoginFalse() {
        _kakaoLoginMutableLiveData.value = false
    }
}