package com.irfan.storyapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.irfan.storyapp.data.model.remote.story.list.ListStoryItem

class StoryDiffCallback(
    private val oldListStories: ArrayList<ListStoryItem>,
    private val newListStories: List<ListStoryItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldListStories.size

    override fun getNewListSize(): Int = newListStories.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldListStories[oldItemPosition] == newListStories[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStory = oldListStories[oldItemPosition]
        val newStory = newListStories[newItemPosition]

        return oldStory.id == newStory.id && oldStory.description == newStory.description
    }


}
