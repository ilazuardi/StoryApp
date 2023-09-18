package com.irfan.storyapp.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfan.storyapp.data.model.remote.auth.LoginResponse
import com.irfan.storyapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> =
        userRepository.userLogin(email, password)

    fun saveUserToken(token: String) {
        viewModelScope.launch {
            userRepository.saveUserToken(token)
        }
    }

}