package com.piri.probodia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.piri.probodia.BuildConfig
import com.piri.probodia.repository.PreferenceRepository
import com.piri.probodia.widget.utils.Version
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel() {

    private val _versionRunnable = MutableLiveData<Boolean>()
    val versionRunnable : LiveData<Boolean>
        get() = _versionRunnable

    fun getVersionRunnable(preferenceRepo : PreferenceRepository) = viewModelScope.launch(coroutineExceptionHandler) {
        try {
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _versionRunnable.value = serverRepository.getVersionRunnable(accessToken, Version.versionCode)
        } catch (e : Exception) {
            refreshApiToken(preferenceRepo)
            val accessToken = preferenceRepo.getApiToken().apiAccessToken
            _versionRunnable.value = serverRepository.getVersionRunnable(accessToken, Version.versionCode)
        }
    }
}