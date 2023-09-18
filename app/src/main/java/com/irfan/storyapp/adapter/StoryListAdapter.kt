package com.irfan.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irfan.storyapp.data.model.local.Story
import com.irfan.storyapp.data.model.remote.story.list.ListStoryItem
import com.irfan.storyapp.databinding.ItemListStoryBinding
import com.irfan.storyapp.ui.detail.DetailActivity
import com.irfan.storyapp.utils.DateFormatter
import java.util.TimeZone

class StoryListAdapter : PagingDataAdapter<Story, StoryListAdapter.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) holder.bind(story)
    }

    inner class ViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(imageItemListStoryIv)
                titleItemListStoryTv.text = story.name
                descItemListStoryTv.text = story.description
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dateItemListStoryTv.text = DateFormatter.formatDate(story.createdAt, TimeZone.getDefault().id)
                } else {
                    dateItemListStoryTv.text = DateFormatter.formatDateFromISOInstantToSimpleFormat(story.createdAt, "yyyy-MM-dd")
                }

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
}