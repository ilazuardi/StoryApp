package com.irfan.storyapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.irfan.storyapp.data.model.local.RemoteKeys
import com.irfan.storyapp.data.model.local.Story

@Database(
    entities = [Story::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}