package com.firman.directoryuser.feature.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.directoryuser.feature.user.domain.usecase.FetchUsersUseCase
import com.firman.directoryuser.feature.user.domain.usecase.GetCitiesUseCase
import com.firman.directoryuser.feature.user.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var loadJob: Job? = null
    private val pageSize = 10

    init {
        loadUsers(reset = true)
        fetchUsers()
        fetchCities()
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query, currentPage = 1, isEndReached = false) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            loadUsers(reset = true)
        }
    }

    fun onCityFilterChange(city: String?) {
        _state.update { it.copy(selectedCity = city, currentPage = 1, isEndReached = false) }
        loadUsers(reset = true)
    }

    fun onSortChange(isAscending: Boolean) {
        _state.update { it.copy(isAscending = isAscending, currentPage = 1, isEndReached = false) }
        loadUsers(reset = true)
    }

    fun onRefresh() {
        fetchUsers(isRefresh = true)
    }

    fun loadNextPage() {
        if (_state.value.isLoading || _state.value.isRefreshing || _state.value.isEndReached) return

        _state.update { it.copy(currentPage = it.currentPage + 1, isLoading = true) }

        viewModelScope.launch {
            delay(1000)
            loadUsers()
        }
    }

    fun onClearFilters() {
        _state.update { 
            it.copy(
                selectedCity = null,
                isAscending = null,
                currentPage = 1,
                isEndReached = false
            ) 
        }
        loadUsers(reset = true)
    }

    private fun fetchUsers(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update { if (isRefresh) it.copy(isRefreshing = true) else it.copy(isLoading = true) }
            val result = fetchUsersUseCase()

            _state.update { it.copy(currentPage = 1, isEndReached = false) }

            loadUsers(reset = true)

            if (result.isFailure && !isRefresh) {
                val currentState = _state.value
                if (currentState.users.isEmpty() && currentState.searchQuery.isEmpty() && currentState.selectedCity == null) {
                    _state.update { it.copy(error = result.exceptionOrNull()?.message ?: "Unknown error") }
                }
            } else {
                _state.update { it.copy(error = null) }
            }

            _state.update { it.copy(isLoading = false, isRefreshing = false) }
        }
    }

    private fun loadUsers(reset: Boolean = false) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val currentState = _state.value
            val users = getUsersUseCase(
                query = currentState.searchQuery,
                city = currentState.selectedCity,
                isAsc = currentState.isAscending,
                page = currentState.currentPage,
                pageSize = pageSize,
            )
            _state.update { 
                it.copy(
                    users = if (reset) users else it.users + users,
                    isEndReached = users.size < pageSize,
                    isLoading = false
                )
            }
        }
    }

    private fun fetchCities() {
        viewModelScope.launch {
            val result = getCitiesUseCase()
            if (result.isSuccess) {
                _state.update { it.copy(cities = result.getOrDefault(emptyList())) }
            }
        }
    }
}
