package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

data class TransactionItem(val icon: Int, val title: String, val subtitle: String, val amount: String, val isIncome: Boolean)

@Composable
fun TransactionHistoryScreen(navController: NavController) {
    val transactions = mapOf(
        "Today" to listOf(
            TransactionItem(R.drawable.money, "Income", "Free Lance project", "+$450", true),
            TransactionItem(R.drawable.car, "Service", "Regular service", "-$150", false)
        ),
        "Yesterday" to listOf(
            TransactionItem(R.drawable.gym, "Gym", "Gym membership", "-$100", false),
            TransactionItem(R.drawable.therapy, "Therapy", "Therapy session", "-$50", false),
            TransactionItem(R.drawable.live_sound, "Live sound gig", "Took a live sound gig", "+$250", true)
        ),
        "Oct 27th" to listOf(
            TransactionItem(R.drawable.bug_image, "Bug bounty program", "Took a bug bounty gig", "+$550", true),
            TransactionItem(R.drawable.drinks_image, "Drinks", "Bought drinks", "-$25", false),
            TransactionItem(R.drawable.gym, "Gym", "Gym membership", "-$100", false),
        TransactionItem(R.drawable.therapy, "Therapy", "Therapy session", "-$50", false),
        TransactionItem(R.drawable.live_sound, "Live sound gig", "Took a live sound gig", "+$250", true)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 28.dp, start = 16.dp, end = 16.dp)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.back_button),
                contentDescription = "Back"
            )
        }
        Text(
            text = "History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth() // Makes the Text composable take the full width
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center // Centers the text horizontally
        )

        LazyColumn {
            transactions.forEach { (date, items) ->
                item {
                    Text(text = date, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                }
                items(items) { transaction ->
                    TransactionItemView(transaction)
                }
            }
        }
    }
}

@Composable
fun TransactionItemView(transaction: TransactionItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = transaction.icon),
            contentDescription = transaction.title,
            modifier = Modifier.size(40.dp)
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = transaction.title, fontWeight = FontWeight.Bold)
            Text(text = transaction.subtitle, fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = transaction.amount,
            color = if (transaction.isIncome) Color(0xFF2E7D32) else Color(0xFFC62828),
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun TransactionHistoryScreenPreview() {
    TransactionHistoryScreen(navController = NavController(LocalContext.current))
}