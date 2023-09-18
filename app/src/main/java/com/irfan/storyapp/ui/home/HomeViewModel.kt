package com.irfan.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.irfan.storyapp.data.model.local.Story
import com.irfan.storyapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getAllStories(token: String): LiveData<PagingData<Story>> =
        storyRepository.getAllStories(token).cachedIn(viewModelScope).asLiveData()

}