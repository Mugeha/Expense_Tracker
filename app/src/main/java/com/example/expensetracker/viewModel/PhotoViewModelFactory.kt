package com.example.expensetracker.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.data.remote.SessionManager

class PhotoViewModelFactory(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(apiService, sessionManager, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
