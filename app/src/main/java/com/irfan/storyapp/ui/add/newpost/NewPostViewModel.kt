package com.irfan.storyapp.ui.add.newpost

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.irfan.storyapp.data.model.remote.story.add.NewStoryResponse
import com.irfan.storyapp.data.repository.StoryRepository
import com.irfan.storyapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
): ViewModel() {

    fun getUserToken(): Flow<String?> = userRepository.getUserToken()

    suspend fun uploadNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Result<NewStoryResponse>> = storyRepository.uploadStory(token, file, description, lat, lon)

}