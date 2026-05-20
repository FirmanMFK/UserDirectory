package com.firman.directoryuser.feature.user.domain.usecase

import com.firman.directoryuser.feature.user.domain.repository.UserRepository

class FetchUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.fetchAndCacheUsers()
}
