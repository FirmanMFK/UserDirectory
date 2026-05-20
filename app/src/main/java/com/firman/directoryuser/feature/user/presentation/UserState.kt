package com.firman.directoryuser.feature.user.presentation

import com.firman.directoryuser.feature.user.domain.model.User

data class UserState(
    val users: List<User> = emptyList(),
    val cities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCity: String? = null,
    val isAscending: Boolean? = null,
    val currentPage: Int = 1,
    val isEndReached: Boolean = false
)
