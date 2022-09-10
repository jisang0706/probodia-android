package com.example.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.probodia.repository.PreferenceRepository
import com.example.probodia.repository.ServerRepository
import com.example.probodia.widget.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

open class TokenViewModel : ViewModel() {

    val serverRepository = ServerRepository()

    private val _isError = SingleLiveEvent<Boolean>()
    val isError : LiveData<Boolean> get() = _isError

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        _isError.call()
    }

    fun refreshApiToken(preferenceRepository : PreferenceRepository) = viewModelScope.launch {
        try {
            val token =
                serverRepository.refreshApiToken(preferenceRepository.getApiToken().apiRefreshToken)
            preferenceRepository.saveAccessToken(token)
        } finally { }
    }
}