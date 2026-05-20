package com.firman.directoryuser.feature.user.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Gender
)

@Serializable
enum class Gender {
    MALE, FEMALE, UNKNOWN
}
