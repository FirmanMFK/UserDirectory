package com.firman.directoryuser.core.di

import com.firman.directoryuser.feature.user.presentation.add.AddUserViewModel
import com.firman.directoryuser.feature.user.presentation.list.UserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { UserViewModel(get(), get(), get()) }
    viewModel { AddUserViewModel(get(), get()) }
}
