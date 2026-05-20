package com.firman.directoryuser.feature.user.data.mapper

import com.firman.directoryuser.feature.user.data.local.entity.UserEntity
import com.firman.directoryuser.feature.user.data.remote.dto.UserDto
import com.firman.directoryuser.feature.user.domain.model.Gender
import com.firman.directoryuser.feature.user.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id ?: "",
        name = name ?: "",
        address = address ?: "",
        email = email ?: "",
        phoneNumber = phoneNumber ?: "",
        city = city ?: "",
        gender = gender ?: 0
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        name = name,
        address = address,
        email = email,
        phoneNumber = phoneNumber,
        city = city,
        gender = when (gender) {
            0 -> Gender.FEMALE
            1 -> Gender.MALE
            else -> Gender.UNKNOWN
        }
    )
}
