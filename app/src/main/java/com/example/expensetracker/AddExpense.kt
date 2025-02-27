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
fun AddExpenseScreen(navController: NavController) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var customCategory by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())) }
    var paymentMethod by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedPayment by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val categories = listOf(
        "Food & Dining" to R.drawable.bibimbap,
        "Transport" to R.drawable.vehicles,
        "Shopping" to R.drawable.shop,
        "Entertainment" to R.drawable.cinema,
        "Other" to R.drawable.add
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Expense",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Enter Amount") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Choose Category") },
                modifier = Modifier.fillMaxWidth(),
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
                categories.forEach { (name, icon) ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(icon),
                                    contentDescription = name,
                                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                                )
                                Text(name)
                            }
                        },
                        onClick = {
                            category = name
                            expandedCategory = false
                        }
                    )
                }
                if (category == "Other") {
                    OutlinedTextField(
                        value = customCategory,
                        onValueChange = { customCategory = it },
                        label = { Text("Enter Custom Category") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Add Note") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date Picker
        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text("Select Date") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
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

        Spacer(modifier = Modifier.height(200.dp))

        val isFormValid = amount.isNotEmpty() && category.isNotEmpty() && note.isNotEmpty() && date.isNotEmpty()

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(
                onClick = { /* Handle save */ },
                enabled = isFormValid,
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Save")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    AddExpenseScreen(navController = NavController(LocalContext.current))
}
