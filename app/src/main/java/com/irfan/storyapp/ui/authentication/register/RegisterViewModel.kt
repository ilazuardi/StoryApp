package com.irfan.storyapp.ui.authentication.register

import androidx.lifecycle.ViewModel
import com.irfan.storyapp.data.model.remote.auth.RegisterResponse
import com.irfan.storyapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> =
        userRepository.userRegister(name, email, password)
}