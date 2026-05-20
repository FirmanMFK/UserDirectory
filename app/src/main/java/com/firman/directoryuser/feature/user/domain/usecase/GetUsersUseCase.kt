package com.firman.directoryuser.feature.user.domain.usecase

import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.domain.repository.UserRepository

class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        query: String?,
        city: String?,
        isAsc: Boolean,
        page: Int,
        pageSize: Int
    ): List<User> {
        return repository.getUsers(query, city, isAsc, page, pageSize)
    }
}
