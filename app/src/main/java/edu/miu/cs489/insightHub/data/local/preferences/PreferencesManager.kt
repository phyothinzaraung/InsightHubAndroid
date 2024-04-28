package edu.miu.cs489.insightHub.data.local.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("INSIGHT_HUB", Context.MODE_PRIVATE)
    val tokenKey: String = "ACCESS_TOKEN"
    val userIdKey: String = "USER_ID"

    fun saveAccessToken(token: String){
        preferences.edit().putString(tokenKey, token).apply()
    }

    fun getAccessToken(): String?{
        return preferences.getString(tokenKey, null)
    }

    fun saveUserId(userId: Int){
        preferences.edit().putInt(userIdKey, userId).apply()
    }

    fun getUserId(): Int? {
        return preferences.getInt(userIdKey, 0)
    }

    fun clear(){
        preferences.edit().clear().apply()
    }
}