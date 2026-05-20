package com.firman.directoryuser.feature.user.data.repository

import com.firman.directoryuser.feature.user.data.local.dao.UserDao
import com.firman.directoryuser.feature.user.data.mapper.toDomain
import com.firman.directoryuser.feature.user.data.mapper.toEntity
import com.firman.directoryuser.feature.user.data.remote.UserService
import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userService: UserService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun fetchAndCacheUsers(): Result<Unit> {
        return try {
            val dtos = userService.getUsers()
            val entities = dtos.map { it.toEntity() }
            userDao.clearAll()
            userDao.insertUsers(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(
        query: String?,
        city: String?,
        isAsc: Boolean,
        page: Int,
        pageSize: Int
    ): List<User> {
        val offset = (page - 1) * pageSize
        return userDao.getUsers(
            query = if (query.isNullOrBlank()) null else query,
            city = if (city.isNullOrBlank()) null else city,
            isAsc = if (isAsc) 1 else 0,
            limit = pageSize,
            offset = offset
        ).map { it.toDomain() }
    }

    override fun getCities(): Flow<List<String>> = userDao.getCities()
}
