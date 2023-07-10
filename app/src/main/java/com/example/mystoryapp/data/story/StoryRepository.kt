package com.example.mystoryapp.data.story

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.data.remote.response.ListStoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiService

class StoryRepository (private val apiService: ApiService) {
    fun getStory(token: String): LiveData<PagingData<ListStoryResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}