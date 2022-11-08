package com.irfan.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irfan.storyapp.data.model.remote.story.list.ListStoryItem
import com.irfan.storyapp.databinding.ItemListStoryBinding
import com.irfan.storyapp.ui.detail.DetailActivity

class StoryListAdapter : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {

    private val listStory = ArrayList<ListStoryItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    inner class ViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(imageItemListStoryIv)
                titleItemListStoryTv.text = story.name
                descItemListStoryTv.text = story.description

                storyRowCv.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.putExtra("title", story.name)
                    intent.putExtra("desc", story.description)
                    intent.putExtra("photo", story.photoUrl)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imageItemListStoryIv, "photo"),
                            Pair(titleItemListStoryTv, "title"),
                            Pair(descItemListStoryTv, "desc"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    fun setListStories(listStories: List<ListStoryItem>) {
        val diffCallback = StoryDiffCallback(this.listStory, listStories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listStory.clear()
        this.listStory.addAll(listStories)
        diffResult.dispatchUpdatesTo(this)
    }
}