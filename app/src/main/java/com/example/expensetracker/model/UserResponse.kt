package com.example.expensetracker.model


data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val profileImage: String,
    val token: String
)
