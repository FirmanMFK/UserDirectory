package com.firman.directoryuser.feature.user.domain.model

data class User(
    val id: String,
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Gender
)

enum class Gender {
    MALE, FEMALE, UNKNOWN
}
