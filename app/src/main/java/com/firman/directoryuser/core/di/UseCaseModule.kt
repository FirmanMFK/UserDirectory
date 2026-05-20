package com.firman.directoryuser.core.di

import com.firman.directoryuser.feature.user.domain.usecase.AddUserUseCase
import com.firman.directoryuser.feature.user.domain.usecase.FetchUsersUseCase
import com.firman.directoryuser.feature.user.domain.usecase.GetCitiesUseCase
import com.firman.directoryuser.feature.user.domain.usecase.GetUsersUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { FetchUsersUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { GetCitiesUseCase(get()) }
    factory { AddUserUseCase(get()) }
}
