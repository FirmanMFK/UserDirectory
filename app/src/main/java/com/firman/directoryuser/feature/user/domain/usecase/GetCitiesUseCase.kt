package com.firman.directoryuser.feature.user.domain.usecase

import com.firman.directoryuser.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCitiesUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<String>> = repository.getCities()
}
