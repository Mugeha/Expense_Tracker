package com.example.expensetracker

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // Import Material 3 components
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
import java.util.Calendar


// Auto-suggest category based on amount
fun suggestExpenseCategory(amount: String): String {
    return when {
        amount.toIntOrNull() ?: 0 > 50000 -> "Investment"
        amount.toIntOrNull() ?: 0 > 10000 -> "Salary"
        else -> "Freelance"
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val focusedBorderColor = Color(0xFF336B40) // Set the desired focused border color in hex format

    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedPayment by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Add Expense",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Enter Amount") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = focusedBorderColor,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = focusedBorderColor,

            ),

        )


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
                        modifier = Modifier.size(24.dp).clickable { expandedCategory = true }
                    )
                }
            )
            DropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                listOf("Shopping", "Entertainment", "Food", "Other").forEach { option ->
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = getExpenseCategoryIcon(option)),
                                contentDescription = option,
                                modifier = Modifier.size(24.dp).padding(end = 8.dp)
                            )
                            Text(option)
                        }
                    }, onClick = {
                        category = option
                        expandedCategory = false
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

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Add Note") },

            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = focusedBorderColor,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = focusedBorderColor,
            ),
            shape = RoundedCornerShape(25.dp)
        )

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

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = paymentMethod,
                onValueChange = {},
                label = { Text("Payment Method") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = focusedBorderColor,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = focusedBorderColor,
                ),
                shape = RoundedCornerShape(25.dp),
                readOnly = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.down_chevron),
                        contentDescription = "Dropdown Icon",
                        modifier = Modifier.size(24.dp).clickable { expandedPayment = true }
                    )
                }
            )
            DropdownMenu(expanded = expandedPayment, onDismissRequest = { expandedPayment = false }) {
                listOf("Cash", "Card", "Mobile Payment").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        paymentMethod = option
                        expandedPayment = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(150.dp))

        val isFormValid = amount.isNotEmpty() && category.isNotEmpty() && note.isNotEmpty() && date.isNotEmpty() && paymentMethod.isNotEmpty()

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            FilledButton(
                title = "Save",
                destination = "expenses-screen",
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

fun getExpenseCategoryIcon(category: String): Int {
    return when (category) {
        "Shopping" -> R.drawable.shop
        "Entertainment" -> R.drawable.cinema
        "Food" -> R.drawable.investment
        else -> R.drawable.add
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    AddExpenseScreen(navController = NavController(LocalContext.current))
}
