package com.example.expensetracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R

@Composable
fun BottomNavigationBar(navController: NavController, selectedItem: String, modifier: Modifier = Modifier) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.home),
        BottomNavItem("Expenses", R.drawable.expenses_white),
        BottomNavItem("History", R.drawable.white_book),
        BottomNavItem("Account", R.drawable.setting_white)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color(0xFF2C4743), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item.title == selectedItem

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable {
                        // Navigate to the corresponding screen
                        when (item.title) {
                            "Home" -> navController.navigate("home-screen")
                            "Expenses" -> navController.navigate("expenses-screen")
                            "History" -> navController.navigate("transaction-history")
                            "Account" -> navController.navigate("account-page")
                        }
                    }
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier.size(34.dp)
                        ) {
                            // Apply the selected effect to any icon
                            Image(
                                painter = painterResource(id = item.icon), // Dynamically apply the selected icon
                                contentDescription = item.title,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}




