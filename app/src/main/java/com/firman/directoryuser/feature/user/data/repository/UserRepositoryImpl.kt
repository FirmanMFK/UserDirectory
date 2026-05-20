package com.firman.directoryuser.feature.user.data.repository

import com.firman.directoryuser.feature.user.data.local.dao.UserDao
import com.firman.directoryuser.feature.user.data.local.entity.CityEntity
import com.firman.directoryuser.feature.user.data.mapper.toDomain
import com.firman.directoryuser.feature.user.data.mapper.toEntity
import com.firman.directoryuser.feature.user.data.remote.UserService
import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userService: UserService,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun fetchAndCacheUsers(): Result<Unit> {
        return try {
            val dtos = userService.getUsers()
            val entities = dtos.map { it.toEntity() }
            userDao.clearAndInsertUsers(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(
        query: String?,
        city: String?,
        isAsc: Boolean?,
        page: Int,
        pageSize: Int
    ): List<User> {
        val offset = (page - 1) * pageSize
        val isAscInt = when (isAsc) {
            true -> 1
            false -> 0
            null -> null
        }
        return userDao.getUsers(
            query = if (query.isNullOrBlank()) null else query,
            city = if (city.isNullOrBlank()) null else city,
            isAsc = isAscInt,
            limit = pageSize,
            offset = offset
        ).map { it.toDomain() }
    }

    override suspend fun getCities(): Result<List<String>> {
        return try {
            val citiesFromNetwork = userService.getCities().mapNotNull { it.name }
            userDao.clearAllCities()
            userDao.insertCities(citiesFromNetwork.map { CityEntity(it) })
            Result.success(citiesFromNetwork)
        } catch (e: Exception) {
            val cachedCities = userDao.getCachedCities().map { it.name }
            if (cachedCities.isNotEmpty()) {
                Result.success(cachedCities)
            } else {
                Result.failure(e)
            }
        }
    }
}
