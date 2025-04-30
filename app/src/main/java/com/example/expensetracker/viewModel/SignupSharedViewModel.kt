package com.example.expensetracker.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SignupSharedViewModel : ViewModel() {
    var username = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")

    fun setUsername(value: String) { username.value = value }
    fun setEmail(value: String) { email.value = value }
    fun setPassword(value: String) { password.value = value }
}
