package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeaserViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // ✅ UI State: Tracks navigation direction
    private val _navigateTo = MutableStateFlow<String?>(null)
    val navigateTo = _navigateTo.asStateFlow()

    init {
        checkAuthToken() // Automatically check on ViewModel creation
    }

    // ✅ Validate or Refresh Token
    private fun checkAuthToken() {
        viewModelScope.launch {
            try {
                val validToken = authRepository.getValidToken()
                _navigateTo.value = if (validToken != null) "home-screen" else "login-screen"
            } catch (e: Exception) {
                _navigateTo.value = "login-screen"
            }
        }
    }
}
