package com.firman.directoryuser.core.di

import com.firman.directoryuser.feature.user.data.repository.UserRepositoryImpl
import com.firman.directoryuser.feature.user.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
}
