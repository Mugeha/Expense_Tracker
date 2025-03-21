package com.example.expensetracker

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    fun signUp(email: String, username: String, password: String, context: Context, navController: NavController) {
        if (isLoading) return
        isLoading = true
        Log.d("SignupViewModel", "Attempting signup with email: $email, username: $username")

        viewModelScope.launch {
            val result = authRepository.signUp(email, username, password)

            result.onSuccess { token ->
                authRepository.saveAuthToken(context, token)
                Log.d("SignupViewModel", "Signup successful! Token: $token")

                signupResult = "Signup Successful"
                navController.navigate("home-screen")
            }.onFailure { error ->
                Log.e("SignupViewModel", "Signup failed: ${error.message}")
                signupResult = error.message ?: "Signup failed. Try again."
            }

            isLoading = false
        }
    }
}

