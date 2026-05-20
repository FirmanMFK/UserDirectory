package com.firman.directoryuser.feature.user.data.remote

import com.firman.directoryuser.feature.user.data.remote.dto.CityDto
import com.firman.directoryuser.feature.user.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET("user")
    suspend fun getUsers(): List<UserDto>

    @POST("user")
    suspend fun createUser(@Body user: UserDto): UserDto

    @GET("city")
    suspend fun getCities(): List<CityDto>
}
