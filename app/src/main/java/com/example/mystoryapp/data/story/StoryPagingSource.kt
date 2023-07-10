package com.example.mystoryapp.data.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp.data.remote.response.ListStoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiService

class StoryPagingSource (private val apiService: ApiService, token: String) : PagingSource<Int, ListStoryResponse>() {
    companion object {
        private const val INITIAL_PAGE_INDEX = 1
    }
    private val token = "Bearer $token"

    override fun getRefreshKey(state: PagingState<Int, ListStoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryResponse> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStory(token, position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}