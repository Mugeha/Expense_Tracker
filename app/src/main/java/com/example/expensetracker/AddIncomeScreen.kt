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
fun AddIncomeScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

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
                text = "Add Income",
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
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Enter Amount") },
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

        // Category Dropdown
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Choose Category") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray, // Gray border when not focused
                    focusedBorderColor = Color.Gray, // Gray border when focused,
                            unfocusedLabelColor = Color.Gray, // Gray label when not focused
                    focusedLabelColor = Color.Gray //
                ),
                shape = RoundedCornerShape(20.dp),// Adjust the corner radius here
                readOnly = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.down_chevron), // Replace with your dropdown icon
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { expanded = true }
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Salary", "Freelance", "Investment").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        category = option
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Note Input
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Add Note") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray, // Gray border when not focused
                focusedBorderColor = Color.Gray,
                unfocusedLabelColor = Color.Gray, // Gray label when not focused
                focusedLabelColor = Color.Gray //// Gray border when focused
            ),
            shape = RoundedCornerShape(20.dp) // Adjust the corner radius here
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
       FilledButton(title = "Save", destination = "HomeScreen", navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun AddIncomeScreenPreview() {
    AddIncomeScreen(navController = NavController(LocalContext.current))
}
