package com.irfan.storyapp.di

import com.irfan.storyapp.data.network.ApiConfig
import com.irfan.storyapp.data.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()

}