package com.firman.directoryuser

import android.app.Application
import androidx.work.*
import com.firman.directoryuser.core.di.*
import com.firman.directoryuser.feature.user.data.worker.RefreshUserWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            workManagerFactory()
            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                workerModule
            )
        }
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshUserWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RefreshUserWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}
