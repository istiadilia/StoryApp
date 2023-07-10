package com.example.mystoryapp

import com.example.mystoryapp.data.remote.response.ListStoryResponse

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryResponse> {
        val items: MutableList<ListStoryResponse> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryResponse(
                i.toString(),
                "name $i",
                "desc $i",
                "photo $i",
                "createdat $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}