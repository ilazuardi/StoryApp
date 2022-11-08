package com.irfan.storyapp.data.model.remote.story.list

import com.google.gson.annotations.SerializedName

data class ListStoryResponse(

    @field:SerializedName("listStory")
    val listStory: ArrayList<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
