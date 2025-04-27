package com.example.expensetracker.model

data class LoginResponse(
    val token: String,
    val username: String,
    val profileImage: String
)
