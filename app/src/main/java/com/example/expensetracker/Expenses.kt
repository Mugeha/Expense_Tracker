package com.example.expensetracker


data class ExpenseItem(
    val id: String, // Add this line to uniquely identify each expense
    val iconRes: Int,
    val title: String,
    val amount: String,
    val time: String
)

