package com.example.expensetracker.viewModel

import AuthRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.remote.SessionManager
import com.example.expensetracker.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _signupResult = MutableStateFlow<Result<Unit>?>(null)
    val signupResult: StateFlow<Result<Unit>?> = _signupResult

    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginResult: StateFlow<Result<LoginResponse>?> = _loginResult

    private val _uploadResult = MutableStateFlow<Result<String>?>(null) // String = uploaded image URL
    val uploadResult: StateFlow<Result<String>?> = _uploadResult

    // 🔐 Save the sessionManager instance for later use
    private var sessionManager: SessionManager? = null

    fun setSessionManager(manager: SessionManager) {
        sessionManager = manager
    }

    fun signup(name: String, email: String, password: String, imageFile: File? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.signup(name, email, password, imageFile)
            _signupResult.value = result.map { Unit }
            _isLoading.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            _loginResult.value = result
            _isLoading.value = false
        }
    }

    fun uploadProfilePhoto(photoFile: File) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.uploadProfilePhoto(photoFile)
            _uploadResult.value = result

            // ✅ Save profile image URL to session
            result.onSuccess { imageUrl ->
                sessionManager?.saveProfileImage(imageUrl)
                Log.d("AuthViewModel", "Saved profile image to session: $imageUrl")
            }.onFailure {
                Log.e("AuthViewModel", "Upload failed: ${it.message}")
            }

            _isLoading.value = false
        }
    }
}
