package com.piri.probodia.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.piri.probodia.BuildConfig
import com.piri.probodia.R
import com.piri.probodia.databinding.ActivityIntroBinding
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.viewmodel.IntroViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.piri.probodia.data.remote.api.*
import com.piri.probodia.view.fragment.record.ServerConnectFailFragment

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    private lateinit var viewModel: IntroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RetrofitAIServerInstance.initInstance(applicationContext)
        RetrofitServerInstance.initInstance(applicationContext)
        RetrofitServerFoodInstance.initInstance(applicationContext)
        RetrofitAIGlucoseServerService.initInstance(applicationContext)
        RetrofitServerChallengeInstance.initInstance(applicationContext)

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
        KakaoSdk.init(applicationContext, BuildConfig.KAKAO_NATIVE_APP_KEY)
        viewModel = ViewModelProvider(this).get(IntroViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.autoLogin()

        viewModel.kakaoLogin.observe(this) {
            kakaoLogin()
        }

        viewModel.kakaoUserId.observe(this, Observer {
            if (it != 0.toLong()) {
                if (viewModel.kakaoAccessToken.value == "") {
                    kakaoLogin()
                } else {
                    viewModel.apiLogin()
                }
            }
        })

        viewModel.kakaoAccessToken.observe(this, Observer {
            if (it != "") {
                if (viewModel.kakaoUserId.value == 0.toLong()) {
                    viewModel.autoLogin()
                } else {
                    viewModel.apiLogin()
                }
            }
        })

        viewModel.buttonLogin.observe(this) {
            binding.kakaoLoginButton.visibility = View.VISIBLE
        }

        viewModel.apiToken.observe(this) {
            viewModel.saveApiToken(PreferenceRepository(applicationContext), it)
        }

        viewModel.join.observe(this) {
            if (it) {
                viewModel.putUserData(
                    PreferenceRepository(applicationContext),
                    viewModel.kakaoUserId.value.toString(),
                    0,
                    "W",
                    0,
                    0,
                    "2형 당뇨"
                )
            } else {
                goMain()
            }
        }

        viewModel.joinResult.observe(this) {
            goMain()
        }

        viewModel.isError.observe(this) {
            popUpServerConnectFail()
        }
    }

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error == null && token != null) {
                viewModel.setKakaoAccessToken(token.accessToken)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@IntroActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@IntroActivity) { token, error ->
                if (error != null) {

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
                } else if (token != null) {
                    // 로그인 성공
                    viewModel.setKakaoAccessToken(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
        }
    }

    fun goMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goJoin() {
        val intent = Intent(applicationContext, JoinBaseInfoActivity::class.java)
        intent.putExtra("USERID", viewModel.kakaoUserId.value)
        intent.putExtra("KAKAOACCESS", viewModel.kakaoAccessToken.value)
        startActivity(intent)
        finish()
    }

    fun popUpServerConnectFail() {
        val fragment = ServerConnectFailFragment(::finish)
        fragment.show(supportFragmentManager, fragment.tag)
    }
}