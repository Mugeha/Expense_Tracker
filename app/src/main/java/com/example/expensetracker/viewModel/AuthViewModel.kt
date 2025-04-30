package com.example.expensetracker.viewModel

import AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.model.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _signupResult = MutableStateFlow<Result<Unit>?>(null)
    val signupResult: StateFlow<Result<Unit>?> = _signupResult

    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginResult: StateFlow<Result<LoginResponse>?> = _loginResult

    private val _uploadResult = MutableStateFlow<Result<Unit>?>(null)
    val uploadResult: StateFlow<Result<Unit>?> = _uploadResult

    fun signup(name: String, email: String, password: String, imageFile: File? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.signup(name, email, password, imageFile)
            _signupResult.value = result.map { Unit } // convert to Unit to avoid leaking response
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
            val result = authRepository.uploadProfileImage(photoFile)
            _uploadResult.value = result
            _isLoading.value = false
        }
    }
}
