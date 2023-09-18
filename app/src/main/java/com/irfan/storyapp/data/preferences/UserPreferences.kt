package com.irfan.storyapp.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserPreferences @Inject constructor(private val dataStore: DataStore<Preferences>){

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_data")
    }

    fun getUserToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }


}