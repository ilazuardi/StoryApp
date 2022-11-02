package com.irfan.storyapp.ui.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irfan.storyapp.data.model.remote.auth.LoginResponse
import com.irfan.storyapp.data.model.remote.auth.LoginResult
import com.irfan.storyapp.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _user = MutableLiveData<LoginResult>()
    val user: LiveData<LoginResult> = _user

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        ApiConfig.getApiService().postLogin(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (!response.isSuccessful) {
                        _message.value = response.message()
                        _error.value = true
                    } else {
                        _user.value = response.body()?.loginResult
                        _message.value = response.body()?.message
                        _error.value = response.body()?.error
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = t.message
                    _error.value = true
                }
            })
    }

}