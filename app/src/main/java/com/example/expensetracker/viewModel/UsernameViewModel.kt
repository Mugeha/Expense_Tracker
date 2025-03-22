package com.example.expensetracker.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsernameViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _username = MutableStateFlow("Loading...")
    val username: StateFlow<String> = _username

    init {
        fetchUsername() // Automatically fetch the username when ViewModel is created
    }

    // ✅ Updated fetchUsername method
    private fun fetchUsername() {
        viewModelScope.launch {
            val token = authRepository.getValidToken()
            if (token != null) {
                // Log token retrieval success
                println("✅ Token found: $token")

                val result = authRepository.fetchUsername()
                result.onSuccess { fetchedUsername ->
                    println("✅ Fetched username: $fetchedUsername")
                    _username.value = fetchedUsername
                }.onFailure { error ->
                    println("❌ Failed to fetch username: ${error.message}")
                    _username.value = "Guest"
                }
            } else {
                println("❌ No valid token found")
                _username.value = "Guest"
            }
        }
    }

}
