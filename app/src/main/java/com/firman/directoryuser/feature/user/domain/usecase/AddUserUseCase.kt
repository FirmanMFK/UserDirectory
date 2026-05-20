package com.firman.directoryuser.feature.user.domain.usecase

import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.domain.repository.UserRepository

class AddUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<User> = repository.createUser(user)
}
