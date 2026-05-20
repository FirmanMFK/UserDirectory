package com.firman.directoryuser.core.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.firman.directoryuser.BuildConfig
import com.firman.directoryuser.feature.user.data.remote.UserService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(androidContext()).build())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(UserService::class.java) }
}
