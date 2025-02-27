package com.example.expensetracker


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R

fun getExpenseById(expenseId: String?): Expense? {
    val expensesList = listOf(
        Expense("1", "15", "Food", "Lunch at Cafe", "23/02/2025", "Card"),
        Expense("2", "50", "Transport", "Bus fare", "24/02/2025", "Cash")
    )
    return expensesList.find { it.id == expenseId }
}

@Composable
fun ExpensesScreen(navController: NavController) {
    val filters = listOf("This week", "This month", "This year")
    var selectedFilter by remember { mutableStateOf(filters[0]) }

    val todayExpenses = listOf(
        ExpenseItem(R.drawable.veggies, "Food", "-$15"),
        ExpenseItem(R.drawable.fuel, "Fuel", "-$5"),
        ExpenseItem(R.drawable.shop, "Shopping", "-$30"),
        ExpenseItem(R.drawable.gym, "Transport", "-$12"),
        ExpenseItem(R.drawable.therapy, "Internet", "-$20")
    )

    val yesterdayExpenses = listOf(
        ExpenseItem(R.drawable.salon, "Salon", "-$50"),
        ExpenseItem(R.drawable.rent, "Rent", "-$400"),
        ExpenseItem(R.drawable.electricity, "Electricity", "-$60"),
        ExpenseItem(R.drawable.insurance, "Insurance", "-$100"),
        ExpenseItem(R.drawable.groceries, "Entertainment", "-$25")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Expenses",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start, // Center the text horizontally
                modifier = Modifier.fillMaxWidth().padding(start = 60.dp) // Ensure the text takes full width
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pie Chart Card (Placeholder)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Adds shadow
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start), // Top color from colors.xml
                                colorResource(id = R.color.gradient_end) // Bottom color from colors.xml
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pie Chart Image
                    Image(
                        painter = painterResource(id = R.drawable.pie_chart),
                        contentDescription = "Pie Chart",
                        modifier = Modifier
                            .size(150.dp) // Adjust size as needed
                    )

                    Spacer(modifier = Modifier.width(58.dp)) // Space between image and labels

                    // Column for Labels
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp), // Space between text items
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Rent Label
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        Color(0xFFFFC107),
                                        shape = CircleShape
                                    ) // Yellow for Rent
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Rent",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Electricity Label
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        Color(0xFF5a8299),
                                        shape = CircleShape
                                    ) // Blue for Electricity
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Electricity",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Insurance Label
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        Color(0xFF4CAF50),
                                        shape = CircleShape
                                    ) // Green for Insurance
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Insurance",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        // Fuel Label
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        Color(0xFFF44336),
                                        shape = CircleShape
                                    ) // Red for Fuel
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Fuel",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                filters.forEach { filter ->
                    FilterButton(filter, filter == selectedFilter) {
                        selectedFilter = filter
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Expenses List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { // Adjust 8.dp to your desired spacing
            item {
                Text("Today", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            items(todayExpenses) { expense ->
                ExpenseRow(expense, navController = navController)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Yesterday", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            items(yesterdayExpenses) { expense ->
                ExpenseRow(expense, navController = navController)
            }
        }
        }
    }

// 🔹 Expense Data Model


    // 🔹 Expense Row UI
    @Composable
    fun ExpenseRow(expense: ExpenseItem, navController: NavController) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.LightGray, // Light gray background
                    shape = RoundedCornerShape(10.dp) // Rounded corners
                )
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = expense.iconRes),
                contentDescription = expense.title,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(expense.title, modifier = Modifier.weight(1f), fontSize = 16.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(expense.amount, fontSize = 16.sp, color = Color.Red)
            IconButton(onClick = {
                val expense = Expense("1", "15", "Food", "Lunch at Cafe", "23/02/2025", "Card")

                navController.navigate("edit-expense/${expense.id}")
            }) {
                Image(
                    painter = painterResource(id = R.drawable.edit_image_2),
                    contentDescription = "Edit Image",
                    modifier = Modifier.alpha(0.5f).size(24.dp) // Set opacity to 50%
                )
            }
        }
    }


    @Composable
    fun FilterButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color.Gray else Color.LightGray,
                contentColor = Color.White
            ),

            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.width(100.dp)
        ) {
            Text(text, fontSize = 14.sp, textAlign = TextAlign.Center, color = Color.Black)

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun ExpensesScreenPreview() {
        ExpensesScreen(navController = NavController(LocalContext.current))
    }

