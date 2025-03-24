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

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email


    init {
        fetchUserData() // Automatically fetch the username when ViewModel is created
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            val token = authRepository.getValidToken()
            if (token != null) {
                println("✅ Token found: $token")

                val usernameResult = authRepository.fetchUsername()
                val emailResult = authRepository.fetchEmail()

                // Handle Username Result
                usernameResult.onSuccess { fetchedUsername ->
                    println("Fetched username: $fetchedUsername")
                    _username.value = fetchedUsername
                }.onFailure { error ->
                    println("Failed to fetch username: ${error.message}")
                    _username.value = "Guest"
                }

                // Handle Email Result
                emailResult.onSuccess { fetchedEmail ->
                    println("Fetched email: $fetchedEmail")
                    _email.value = fetchedEmail
                }.onFailure { error ->
                    println("Failed to fetch email: ${error.message}")
                    _email.value = "No email available"
                }

            } else {
                println("No valid token found")
                _username.value = "Guest"
                _email.value = "No email available"
            }
        }
    }


}
