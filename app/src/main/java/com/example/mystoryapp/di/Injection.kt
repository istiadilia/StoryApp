package com.example.mystoryapp.di

import com.example.mystoryapp.data.story.StoryRepository
import com.example.mystoryapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()

        return StoryRepository(apiService)
    }
}