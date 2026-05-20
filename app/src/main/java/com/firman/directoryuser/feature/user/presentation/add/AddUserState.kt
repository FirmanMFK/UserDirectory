package com.firman.directoryuser.feature.user.presentation.add

data class AddUserState(
    val name: String = "",
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val address: String = "",
    val city: String = "",
    val gender: Int = 0, // 0: Male, 1: Female
    val cities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
