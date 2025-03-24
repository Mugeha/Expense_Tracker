package com.example.expensetracker.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.expensetracker.repository.AuthRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var signupResult by mutableStateOf<String?>(null)
        private set

    // ✅ SignUp Method: Handles Signup and Navigation
    fun signUp(email: String, username: String, password: String, navController: NavController) {

        if (email.isBlank() || username.isBlank() || password.isBlank()) {
            signupResult = "All fields are required."
            return
        }
        if (password.length < 6) {
            signupResult = "Password must be at least 6 characters long."
            return
        }

        if (isLoading) return
        isLoading = true
        Log.d("SignupViewModel", "Attempting signup with email: $email, username: $username")

        viewModelScope.launch {
            Log.d("SignupViewModel", "Starting signup request...")
            val result = authRepository.signUp(email, username, password)

            result.onSuccess { token ->
                Log.d("SignupViewModel", "Signup successful. Token: $token")

                signupResult = "Signup Successful!"

                // Ensure navigation happens after successful signup
                navController.navigate("addphoto-screen") {
                    popUpTo("signup-screen") { inclusive = true } // Clears signup from the stack
                    launchSingleTop = true // Avoids multiple instances
                }

            }.onFailure { error ->
                Log.e("SignupViewModel", "Signup failed: ${error.message}")
                signupResult = error.message ?: "Signup failed. Try again."
            }

            isLoading = false
        }

    }
}
