package com.firman.directoryuser.core.di

import com.firman.directoryuser.feature.user.data.worker.RefreshUserWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker { RefreshUserWorker(get(), get(), get()) }
}

