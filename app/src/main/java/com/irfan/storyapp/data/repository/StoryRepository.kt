package com.irfan.storyapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.irfan.storyapp.data.model.local.Story
import com.irfan.storyapp.data.model.remote.story.add.NewStoryResponse
import com.irfan.storyapp.data.model.remote.story.list.ListStoryResponse
import com.irfan.storyapp.data.network.ApiService
import com.irfan.storyapp.data.network.StoryRemoteMediator
import com.irfan.storyapp.data.room.StoryDatabase
import com.irfan.storyapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
){

    fun getAllStories(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                apiService,
                "Bearer $token"
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).flow
    }

    fun getAllStoriesWithLocation(token: String): Flow<Result<ListStoryResponse>> = flow {
        wrapEspressoIdlingResource {
            try {
                val bearerToken = "Bearer $token"
                val response = apiService.getStories(bearerToken, size = 30, location = 1)
                emit(Result.success(response))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    suspend fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) : Flow<Result<NewStoryResponse>> {
        return flow {
            try {
                val bearerToken = "Bearer $token"
                val response = apiService.postStory(bearerToken, file, description, lat, lon)
                emit(Result.success(response))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

}