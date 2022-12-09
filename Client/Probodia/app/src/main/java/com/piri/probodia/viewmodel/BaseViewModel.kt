package com.piri.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.repository.ServerRepository
import com.piri.probodia.widget.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    val serverRepository = ServerRepository()

    private val _isError = SingleLiveEvent<Boolean>()
    val isError : LiveData<Boolean> get() = _isError

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _isError.call()
    }

    suspend fun refreshApiToken(preferenceRepository : PreferenceRepository) {
        try {
            val token = preferenceRepository.getApiToken()
            val accessToken = serverRepository.refreshApiToken(token.apiAccessToken, token.apiRefreshToken)
            preferenceRepository.saveAccessToken(accessToken)
        } finally { }
    }
}