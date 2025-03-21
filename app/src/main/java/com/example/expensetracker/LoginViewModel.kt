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

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var emailTouched by mutableStateOf(false)
    var passwordTouched by mutableStateOf(false)
    var loginResult by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // Validate email and password
    val isEmailInvalid: Boolean
        get() = emailTouched && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val isPasswordInvalid: Boolean
        get() = passwordTouched && password.length < 6

    fun performLogin(context: Context, navController: NavController) {
        if (isLoading) return
        isLoading = true
        Log.d("LoginViewModel", "Attempting login with email: $email")

        viewModelScope.launch {
            val result = authRepository.login(email, password)

            result.onSuccess { token ->
                authRepository.saveAuthToken(context, token)
                Log.d("LoginViewModel", "Login successful! Token: $token")

                loginResult = "Login Successful"
                navController.navigate("home-screen")
            }.onFailure { error ->
                Log.e("LoginViewModel", "Login failed: ${error.message}")
                loginResult = error.message ?: "Login failed. Try again."
            }

            isLoading = false
        }
    }

}
