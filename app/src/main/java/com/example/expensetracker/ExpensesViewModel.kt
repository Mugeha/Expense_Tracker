package com.example.expensetracker

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@HiltViewModel
class ExpenseViewModel : ViewModel() {
    var selectedExpense by mutableStateOf<Expense?>(null)
        private set

    fun setExpense(expense: Expense) {
        selectedExpense = expense
    }
}

annotation class HiltViewModel
