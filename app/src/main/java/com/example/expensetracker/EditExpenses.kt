package com.example.expensetracker

import android.app.DatePickerDialog
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
import androidx.navigation.compose.rememberNavController
import java.util.Calendar

// Define Expense data class
data class Expense(
    val id: String,
    val amount: String,
    val category: String,
    val note: String,
    val date: String,
    val paymentMethod: String
)

@Composable
fun EditExpenseScreen(navController: NavController, expense: Expense) {
    var amount by remember { mutableStateOf(expense.amount) }
    var category by remember { mutableStateOf(expense.category) }
    var note by remember { mutableStateOf(expense.note) }
    var date by remember { mutableStateOf(expense.date) }
    var paymentMethod by remember { mutableStateOf(expense.paymentMethod) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedPayment by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top =28.dp),
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
                text = "Edit Expense",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
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
                focusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
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
                listOf("Food", "Fuel", "Rent", "Insurance").forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        category = option
                        expandedCategory = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Add Note") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text("Select Date") },
            modifier = Modifier.fillMaxWidth().clickable {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth -> date = "$dayOfMonth/${month + 1}/$year" },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            shape = RoundedCornerShape(25.dp),
            readOnly = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = paymentMethod,
                onValueChange = {},
                label = { Text("Payment Method") },
                modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(200.dp))

        val isFormValid = amount.isNotEmpty() && category.isNotEmpty() && note.isNotEmpty() && date.isNotEmpty() && paymentMethod.isNotEmpty()

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            FilledButton(
                title = "Save",
onClick = {}            )
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

@Preview(showBackground = true)
@Composable
fun PreviewEditExpenseScreen() {
    EditExpenseScreen(
        navController = rememberNavController(), // Use a dummy NavController
        expense = Expense(
            id = "1",
            amount = "15",
            category = "Food",
            note = "Lunch at Cafe",
            date = "23/02/2025",
            paymentMethod = "Card"
        )
    )
}

