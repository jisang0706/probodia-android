package com.example.probodia.repository

import android.content.Context
import com.example.probodia.data.remote.model.ApiToken

class PreferenceRepository(private val context: Context) {

    val pref = context.getSharedPreferences("probodia", Context.MODE_PRIVATE)

    fun saveApiToken(apiToken : ApiToken) {
        pref.edit().putString("apiAccessToken", apiToken.apiAccessToken).apply()
        pref.edit().putString("apiRefreshToken", apiToken.apiRefreshToken).apply()
    }

    fun getApiToken() : ApiToken = ApiToken (
        pref.getString("apiAccessToken", "").toString(),
        pref.getString("apiRefreshToken", "").toString()
            )
}