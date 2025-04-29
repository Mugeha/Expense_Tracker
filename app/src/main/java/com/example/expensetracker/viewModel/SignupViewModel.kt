package com.example.expensetracker.viewModel

import AuthRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _signupResult = MutableStateFlow<String?>(null)
    val signupResult = _signupResult.asStateFlow()

    fun signUp(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _signupResult.value = "All fields are required."
            return
        }

        if (_isLoading.value) {
            return // already signing up
        }

        _isLoading.value = true

        viewModelScope.launch {
            val result = authRepository.signup(username, email, password)

            result.onSuccess {
                Log.d("SignupViewModel", "Signup successful")
                _signupResult.value = "Signup successful!"
            }.onFailure { error ->
                Log.e("SignupViewModel", "Signup failed: ${error.message}")
                _signupResult.value = error.message ?: "Signup failed. Try again."
            }

            _isLoading.value = false
        }
    }
}
