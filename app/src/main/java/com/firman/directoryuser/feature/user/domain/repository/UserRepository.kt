package com.firman.directoryuser.feature.user.domain.repository

import com.firman.directoryuser.feature.user.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchAndCacheUsers(): Result<Unit>
    suspend fun getUsers(
        query: String?,
        city: String?,
        isAsc: Boolean?,
        page: Int,
        pageSize: Int
    ): List<User>
    suspend fun getCities(): Result<List<String>>
    suspend fun createUser(user: User): Result<User>
}
