package com.firman.directoryuser.core.di

import androidx.room.Room
import com.firman.directoryuser.core.data.local.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "user_directory.db"
        ).build()
    }

    single { get<AppDatabase>().userDao() }
}
