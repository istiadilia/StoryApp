package com.example.mystoryapp.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.story.StoryRepository
import com.example.mystoryapp.data.remote.response.ListStoryResponse

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getAllStory(token: String) : LiveData<PagingData<ListStoryResponse>> {
        return storyRepository
            .getStory(token)
            .cachedIn(viewModelScope)
    }
}