package com.example.expensetracker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
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



// Auto-suggest category based on amount
fun suggestCategory(amount: String): String {
    return when {
        amount.toIntOrNull() ?: 0 > 50000 -> "Investment"
        amount.toIntOrNull() ?: 0 > 10000 -> "Salary"
        else -> "Freelance"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(navController: NavController) {
    val focusedBorderColor = Color(0xFF336B40) // Set the desired focused border color in hex format

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var customCategory by remember { mutableStateOf("") }
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
            style = MaterialTheme.typography.titleLarge,
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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusedBorderColor
            ),
            label = { Text("Enter Amount") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown with Images
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = focusedBorderColor
                ),
                label = { Text("Choose Category") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                readOnly = true,

                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.down_chevron),
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.size(24.dp).clickable { expanded = true }
                    )
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Salary", "Freelance", "Investment", "Other").forEach { option ->
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = getCategoryIcon(option)),
                                contentDescription = option,
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text(option)
                        }
                    }, onClick = {
                        category = option
                        expanded = false
                    })
                }
            }
        }

        if (category == "Other") {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = customCategory,
                onValueChange = { customCategory = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = focusedBorderColor
                ),
                label = { Text("Enter Custom Category") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),

            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker
        OutlinedTextField(
            value = date,
            onValueChange = {},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusedBorderColor
            ),
            label = { Text("Select Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            shape = RoundedCornerShape(25.dp),

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
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusedBorderColor
            ),
            label = { Text("Add Note") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),

        )

        Spacer(modifier = Modifier.height(280.dp))

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

fun getCategoryIcon(category: String): Int {
    return when (category) {
        "Salary" -> R.drawable.money
        "Freelance" -> R.drawable.self_employed
        "Investment" -> R.drawable.investment
        else -> R.drawable.add
    }
}

@Preview(showBackground = true)
@Composable
fun AddIncomeScreenPreview() {
    AddIncomeScreen(navController = NavController(LocalContext.current))
}
