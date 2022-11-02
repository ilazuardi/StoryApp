package com.irfan.storyapp.data.preferences

import android.content.Context
import com.irfan.storyapp.data.model.local.UserSession

class UserPreferences(context: Context) {
    companion object {
        private const val PREF_NAME = "user_pref"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NAME = "name"
        private const val KEY_TOKEN = "token"
        private const val KEY_LOG_STATUS = "log_status"

    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun setUser(session: UserSession) {
        editor.apply {
            putString(KEY_USER_ID, session.userId)
            putString(KEY_NAME, session.name)
            putString(KEY_TOKEN, session.token)
            putBoolean(KEY_LOG_STATUS, session.isLogin)
            apply()
        }
    }

    fun getUser(): UserSession =
        UserSession(
            pref.getString(KEY_USER_ID, "").toString(),
            pref.getString(KEY_NAME, "").toString(),
            pref.getString(KEY_TOKEN, "").toString(),
            pref.getBoolean(KEY_LOG_STATUS, false)
        )

    fun logout() {
        editor.apply {
            remove(KEY_USER_ID)
            remove(KEY_NAME)
            remove(KEY_TOKEN)
            putBoolean(KEY_LOG_STATUS, false)
            apply()
        }
    }


}