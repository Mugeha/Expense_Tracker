package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

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
        ExpenseItem("1", R.drawable.veggies, "Food", "-KSH15", "13:45"),
        ExpenseItem("2", R.drawable.fuel, "Fuel", "-KSH5", "09:30"),
        ExpenseItem("3", R.drawable.shop, "Shopping", "-KSH30", "11:15"),
        ExpenseItem("4", R.drawable.gym, "Transport", "-KSH12", "07:50"),
        ExpenseItem("5", R.drawable.therapy, "Internet", "-KSH20", "18:10")
    )

    val yesterdayExpenses = listOf(
        ExpenseItem("6", R.drawable.salon, "Salon", "-KSH50", "16:00"),
        ExpenseItem("7", R.drawable.rent, "Rent", "-KSH400", "12:30"),
        ExpenseItem("8", R.drawable.electricity, "Electricity", "-KSH60", "14:45"),
        ExpenseItem("9", R.drawable.insurance, "Insurance", "-KSH100", "10:00"),
        ExpenseItem("10", R.drawable.groceries, "Entertainment", "-KSH25", "20:20")
    )





    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Expenses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pie Chart Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
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
                    Image(
                        painter = painterResource(id = R.drawable.pie_chart),
                        contentDescription = "Pie Chart",
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.width(58.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        CategoryLabel("Rent", Color(0xFFFFC107))
                        CategoryLabel("Electricity", Color(0xFF5a8299))
                        CategoryLabel("Insurance", Color(0xFF4CAF50))
                        CategoryLabel("Fuel", Color(0xFFF44336))
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
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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

@Composable
fun CategoryLabel(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ExpenseRow(expense: ExpenseItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = expense.iconRes),
            contentDescription = expense.title,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(expense.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(expense.time, fontSize = 12.sp, color = Color.DarkGray) // Time displayed below title
        }

        Spacer(modifier = Modifier.width(10.dp))
        Text(expense.amount, fontSize = 16.sp, color = Color.Red)

        IconButton(onClick = {
            navController.navigate("edit-expense-screen/${expense.id}")
        }) {
            Image(
                painter = painterResource(id = R.drawable.edit_image_2),
                contentDescription = "Edit Image",
                modifier = Modifier
                    .alpha(0.5f)
                    .size(24.dp)
                    .clickable { navController.navigate("edit-expense-screen/${expense.id}") }
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
    val navController = rememberNavController()
    ExpensesScreen(navController = navController)
}
