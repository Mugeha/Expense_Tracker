package com.example.expensetracker

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.expensetracker.repository.AuthRepository

class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun logout(navController: NavController) {
        authRepository.logout() // Call the repository method

        // Navigate to login and clear the backstack
        navController.navigate("login-screen") {
            popUpTo(0) { inclusive = true }
        }
    }
}
