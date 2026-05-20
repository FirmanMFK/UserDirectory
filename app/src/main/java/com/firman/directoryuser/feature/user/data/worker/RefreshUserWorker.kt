package com.firman.directoryuser.feature.user.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.firman.directoryuser.feature.user.domain.usecase.FetchUsersUseCase

class RefreshUserWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val fetchUsersUseCase: FetchUsersUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val result = fetchUsersUseCase()
            if (result.isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
