package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ChangeDetailsScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button and Title Row
        Column {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Change Personal Details",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth() // Makes the Text composable take the full width
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center // Centers the text horizontally
            )
        }

        // Amount Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                focusedBorderColor = Color.Gray,// Gray border when focused
                unfocusedLabelColor = Color.Gray, // Gray label when not focused
                focusedLabelColor = Color.Gray //
            )// Adjust the corner radius here
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // Note Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter email") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                focusedBorderColor = Color.Gray,
                unfocusedLabelColor = Color.Gray, // Gray label when not focused
                focusedLabelColor = Color.Gray //// Gray border when focused
            ),
            shape = RoundedCornerShape(20.dp) // Adjust the corner radius here
        )

        Spacer(modifier = Modifier.height(430.dp))

        // Save Button
        FilledButton(title = "Save", onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ChangeDetailsScreenPreview() {
    ChangeDetailsScreen(navController = NavController(LocalContext.current))
}
