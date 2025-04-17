package com.example.expensetracker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.api.LoginRequest
import com.example.expensetracker.data.remote.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    // ✅ Login Method
    fun login(email: String, password: String, navController: NavController) {
        Log.d("LoginViewModel", "Login attempt with email: $email")

        if (email.isBlank() || password.isBlank()) {
            Log.w("LoginViewModel", "Empty email or password.")
            _loginResult.value = "Email and password required."
            return
        }

        if (_isLoading.value) {
            Log.d("LoginViewModel", "Login already in progress, ignoring new request.")
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Calling login API...")
                val response = apiService.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val user = response.body()!!
                    Log.d("LoginViewModel", "Login successful. Token: ${user.token}")

                    sessionManager.saveUserSession(user.token, user.username, user.profileImage)
                    Log.d("LoginViewModel", "User session saved. Navigating to home.")

                    _loginResult.value = "Login successful!"

                    navController.navigate("home-screen") {
                        popUpTo("login-screen") { inclusive = true }
                    }
                } else {
                    Log.w("LoginViewModel", "Login failed. Response code: ${response.code()}")
                    _loginResult.value = "Login failed. Check credentials."
                }

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login exception: ${e.message}", e)
                _loginResult.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
                Log.d("LoginViewModel", "Login flow complete. Loading state reset.")
            }
        }
    }
}
