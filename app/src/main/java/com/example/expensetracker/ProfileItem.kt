package com.example.expensetracker

import androidx.compose.ui.graphics.vector.ImageVector

// Data Class for Profile Items
data class ProfileItem(val icon: ImageVector, val title: String, val value: String, val isSwitch: Boolean = false)
