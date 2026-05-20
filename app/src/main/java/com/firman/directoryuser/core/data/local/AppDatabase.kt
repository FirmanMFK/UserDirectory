package com.firman.directoryuser.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.firman.directoryuser.feature.user.data.local.dao.UserDao
import com.firman.directoryuser.feature.user.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
