package com.irfan.storyapp.data.network

import com.irfan.storyapp.data.model.remote.auth.LoginResponse
import com.irfan.storyapp.data.model.remote.auth.RegisterResponse
import com.irfan.storyapp.data.model.remote.story.add.NewStoryResponse
import com.irfan.storyapp.data.model.remote.story.list.ListStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<ListStoryResponse>

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<NewStoryResponse>
}