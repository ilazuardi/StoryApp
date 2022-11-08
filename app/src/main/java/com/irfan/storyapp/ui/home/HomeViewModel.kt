package com.irfan.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.irfan.storyapp.data.model.remote.story.list.ListStoryItem
import com.irfan.storyapp.data.model.remote.story.list.ListStoryResponse
import com.irfan.storyapp.data.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _stories = MutableLiveData<ArrayList<ListStoryItem>>()
    val stories: LiveData<ArrayList<ListStoryItem>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getAllStories(token: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getStories("Bearer $token")
            .enqueue(object : Callback<ListStoryResponse> {
                override fun onResponse(
                    call: Call<ListStoryResponse>,
                    response: Response<ListStoryResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _stories.postValue(response.body()?.listStory)
                        _message.postValue(response.body()?.message)
                    }
                }

                override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = t.message
                }
            })
    }

}