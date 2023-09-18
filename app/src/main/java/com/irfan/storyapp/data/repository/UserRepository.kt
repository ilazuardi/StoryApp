package com.irfan.storyapp.data.repository

import com.irfan.storyapp.data.model.remote.auth.LoginResponse
import com.irfan.storyapp.data.model.remote.auth.RegisterResponse
import com.irfan.storyapp.data.network.ApiService
import com.irfan.storyapp.data.preferences.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = apiService.postRegister(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.postLogin(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveUserToken(token: String) {
        userPreferences.saveUserToken(token)
    }

    fun getUserToken(): Flow<String?> = userPreferences.getUserToken()
}