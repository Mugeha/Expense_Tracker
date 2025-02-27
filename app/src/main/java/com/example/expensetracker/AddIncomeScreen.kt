package com.example.expensetracker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddIncomeScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }

        }
        Text(
            text = "Add Income",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Amount Input
        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
                category = suggestCategory(it) // Auto-suggest category
            },
            label = { Text("Enter Amount") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Choose Category") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                readOnly = true,
                colors = textFieldColors(),
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.down_chevron),
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.size(24.dp).clickable { expanded = true }
                    )
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Salary", "Freelance", "Investment").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        category = option
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker
        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text("Select Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            shape = RoundedCornerShape(25.dp),
            colors = textFieldColors(),
            trailingIcon = {
                Image(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = "Calendar Icon",
                    modifier = Modifier.size(24.dp).clickable {
                        val calendar = Calendar.getInstance()
                        val datePicker = DatePickerDialog(
                            context,
                            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                                date = "$dayOfMonth/${month + 1}/$year"
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Note Input
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Add Note") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = textFieldColors()
        )

        Spacer(modifier = Modifier.height(300.dp))

        // Save Button
        val isFormValid = amount.isNotEmpty() && category.isNotEmpty() && note.isNotEmpty() && date.isNotEmpty()

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            FilledButton(
                title = "Save",
                destination = "home-screen",
                navController = navController
            )
            if (!isFormValid) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Transparent)
                        .clickable(enabled = false) {}
                )
            }
        }
    }
}

// Auto-suggest category based on amount
fun suggestCategory(amount: String): String {
    return when {
        amount.toIntOrNull() ?: 0 > 50000 -> "Investment"
        amount.toIntOrNull() ?: 0 > 10000 -> "Salary"
        else -> "Freelance"
    }
}

// Reusable text field colors
@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Color.Gray,
    focusedBorderColor = Color.Gray,
    unfocusedLabelColor = Color.Gray,
    focusedLabelColor = Color.Gray
)

@Preview(showBackground = true)
@Composable
fun AddIncomeScreenPreview() {
    AddIncomeScreen(navController = NavController(LocalContext.current))
}
