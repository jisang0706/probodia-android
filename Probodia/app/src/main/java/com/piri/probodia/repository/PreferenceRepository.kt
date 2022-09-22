package com.piri.probodia.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.piri.probodia.data.remote.model.ApiToken

class PreferenceRepository(private val context: Context) {

    val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val preference = EncryptedSharedPreferences.create(
        context,
        "probodia",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun saveApiToken(apiToken : ApiToken) {
        preference.edit().putString("apiAccessToken", apiToken.apiAccessToken).apply()
        preference.edit().putString("apiRefreshToken", apiToken.apiRefreshToken).apply()
    }

    suspend fun saveAccessToken(accessToken : String) {
        preference.edit().putString("apiAccessToken", accessToken).apply()
    }

    fun getApiToken() : ApiToken = ApiToken (
        preference.getString("apiAccessToken", "").toString(),
        preference.getString("apiRefreshToken", "").toString(),
        false
    )
}