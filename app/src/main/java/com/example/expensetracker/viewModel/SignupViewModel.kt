package com.example.expensetracker.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.data.remote.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignupViewModel(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) : ViewModel() {

    var isLoading = MutableStateFlow(false)
        private set

    private val _signupResult = MutableStateFlow<String?>(null)
    val signupResult = _signupResult.asStateFlow()

    fun signUp(
        email: String,
        username: String,
        password: String,
        navController: NavController
    ) {
        Log.d("SignupViewModel", "Attempting signup for email: $email, username: $username")

        if (email.isBlank() || username.isBlank() || password.isBlank()) {
            Log.w("SignupViewModel", "Validation failed: One or more fields are blank.")
            _signupResult.value = "All fields are required."
            return
        }

        if (isLoading.value) {
            Log.d("SignupViewModel", "Signup already in progress. Ignoring new request.")
            return
        }

        isLoading.value = true
        Log.d("SignupViewModel", "Sending signup request...")

        viewModelScope.launch {
            try {
                val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
                val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)

                val response = apiService.signup(usernameBody, emailBody, passwordBody)

                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        Log.d("SignupViewModel", "Signup successful. Token: ${user.token}")

                        sessionManager.saveUserSession(user.token, user.username, user.profileImage)
                        Log.d("SignupViewModel", "Session saved. Navigating to add-photo screen.")

                        _signupResult.value = "Signup successful!"

                        navController.navigate("add-photo-screen") {
                            popUpTo("signup-screen") { inclusive = true }
                        }
                    } ?: run {
                        Log.w("SignupViewModel", "Signup failed: Empty response body.")
                        _signupResult.value = "Signup failed. Empty response."
                    }
                } else {
                    Log.w("SignupViewModel", "Signup failed. Code: ${response.code()}")
                    _signupResult.value = "Signup failed. Try again."
                }
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Exception during signup: ${e.message}", e)
                _signupResult.value = "Error: ${e.message}"
            } finally {
                isLoading.value = false
                Log.d("SignupViewModel", "Signup flow finished. Loading state reset.")
            }
        }
    }
}
