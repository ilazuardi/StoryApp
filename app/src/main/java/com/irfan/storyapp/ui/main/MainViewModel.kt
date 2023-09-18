package com.irfan.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irfan.storyapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    fun saveUserToken(token: String) {
        viewModelScope.launch {
            userRepository.saveUserToken(token)
        }
    }

    fun getUserToken(): Flow<String?> = userRepository.getUserToken()

}