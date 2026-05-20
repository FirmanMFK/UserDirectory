package com.firman.directoryuser.feature.user.data.remote

import com.firman.directoryuser.feature.user.data.remote.dto.CityDto
import com.firman.directoryuser.feature.user.data.remote.dto.UserDto
import retrofit2.http.GET

interface UserService {
    @GET("user")
    suspend fun getUsers(): List<UserDto>

    @GET("city")
    suspend fun getCities(): List<CityDto>
}
