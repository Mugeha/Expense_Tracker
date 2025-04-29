package com.example.expensetracker.viewModel

import AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginResult.value = "Email and password are required."
            return
        }

        if (_isLoading.value) return

        _isLoading.value = true

        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess {
                _loginResult.value = "success" // Can check for "success" in Composable and navigate
            }.onFailure {
                _loginResult.value = it.message ?: "Login failed. Try again."
            }
            _isLoading.value = false
        }
    }
}
