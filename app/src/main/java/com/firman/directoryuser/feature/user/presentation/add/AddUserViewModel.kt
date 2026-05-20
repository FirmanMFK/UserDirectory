package com.firman.directoryuser.feature.user.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firman.directoryuser.feature.user.domain.model.Gender
import com.firman.directoryuser.feature.user.domain.model.User
import com.firman.directoryuser.feature.user.domain.usecase.AddUserUseCase
import com.firman.directoryuser.feature.user.domain.usecase.GetCitiesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddUserViewModel(
    private val addUserUseCase: AddUserUseCase,
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddUserState())
    val state: StateFlow<AddUserState> = _state.asStateFlow()

    init {
        fetchCities()
    }

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
        validateEmail(email)
    }

    fun onPhoneChange(phone: String) {
        _state.update { it.copy(phoneNumber = phone) }
    }

    fun onAddressChange(address: String) {
        _state.update { it.copy(address = address) }
    }

    fun onCityChange(city: String) {
        _state.update { it.copy(city = city) }
    }

    fun onGenderChange(gender: Int) {
        _state.update { it.copy(gender = gender) }
    }

    private fun validateEmail(email: String): Boolean {
        val isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (!isValid && email.isNotEmpty()) {
            _state.update { it.copy(emailError = "Please enter a valid email") }
        }
        return isValid
    }

    fun saveUser() {
        val currentState = _state.value
        if (!validateEmail(currentState.email)) return
        if (currentState.name.isBlank()) {
            _state.update { it.copy(error = "Name cannot be empty") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            val user = User(
                id = "", // API will generate ID
                name = currentState.name,
                address = currentState.address,
                email = currentState.email,
                phoneNumber = currentState.phoneNumber,
                city = currentState.city,
                gender = if (currentState.gender == 0) Gender.MALE else Gender.FEMALE
            )

            val result = addUserUseCase(user)
            
            if (result.isSuccess) {
                _state.update { it.copy(isSuccess = true, isLoading = false) }
            } else {
                _state.update { it.copy(error = result.exceptionOrNull()?.message ?: "Failed to save user", isLoading = false) }
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
