package com.firman.directoryuser.feature.user.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firman.directoryuser.feature.user.data.local.entity.CityEntity
import com.firman.directoryuser.feature.user.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Query("""
        SELECT * FROM users 
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%')
        AND (:city IS NULL OR city = :city)
        ORDER BY 
            CASE WHEN :isAsc = 1 THEN name END ASC,
            CASE WHEN :isAsc = 0 THEN name END DESC,
            CASE WHEN :isAsc IS NULL THEN CAST(id AS INTEGER) END ASC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getUsers(
        query: String?,
        city: String?,
        isAsc: Int?,
        limit: Int,
        offset: Int
    ): List<UserEntity>

    @Query("DELETE FROM users")
    suspend fun clearAllUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM cities ORDER BY name ASC")
    suspend fun getCachedCities(): List<CityEntity>

    @Query("DELETE FROM cities")
    suspend fun clearAllCities()
}
