package com.firman.directoryuser.feature.user.domain.usecase

import com.firman.directoryuser.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCitiesUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.getCities()
}
