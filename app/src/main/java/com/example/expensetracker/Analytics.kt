package com.example.expensetracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.compose.rememberNavController

@Composable
fun AnalyticsScreen(navController: NavController) {
    var selectedTimeframe by remember { mutableStateOf("This Week") }
    var selectedCategory by remember { mutableStateOf("All Categories") }
    val timeframes = listOf("This Week", "This Month", "This Year")
    val categories = listOf(
        "Grocery" to R.drawable.groceries,
        "Shop" to R.drawable.shop,
        "Electricity" to R.drawable.electricity,
        "Insurance" to R.drawable.insurance,
        "Transport" to R.drawable.vehicles,
        "Entertainment" to R.drawable.cinema
    )
    var currentCategoryIndex by remember { mutableStateOf(0) }
    var balanceVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp, start = 16.dp, end = 16.dp)
    ) {
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }
//            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Analytics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(3f)
            )
            Spacer(modifier = Modifier.weight(1f)) // Balances spacing
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Balance Card
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
                                colorResource(id = R.color.gradient_start),
                                colorResource(id = R.color.gradient_end)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Balance",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (balanceVisible) "$5,00000" else "********",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Eye Icon Button
                        IconButton(onClick = { balanceVisible = !balanceVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (balanceVisible) R.drawable.view_password_3__1_ else R.drawable.view_password_negative
                                ),
                                contentDescription = if (balanceVisible) "Hide Balance" else "Show Balance",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Growth Indicator Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.increase),
                            contentDescription = "Growth Indicator",
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Row {
                            Text(
                                text = "+35% ",
                                color = colorResource(id = R.color.increaseColor),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Than Last Month",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Timeframe Navigation
        Text(
            text = "Summary",
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Previous Timeframe",
                    modifier = Modifier.size(24.dp).clickable {
                        val currentIndex = timeframes.indexOf(selectedTimeframe)
                        if (currentIndex > 0) selectedTimeframe = timeframes[currentIndex - 1]
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedTimeframe, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.forward_arrow),
                    contentDescription = "Next Timeframe",
                    modifier = Modifier.size(24.dp).clickable {
                        val currentIndex = timeframes.indexOf(selectedTimeframe)
                        if (currentIndex < timeframes.size - 1) selectedTimeframe =
                            timeframes[currentIndex + 1]
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Graph Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Graph: $selectedCategory - $selectedTimeframe", color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Categories Section
        Text("Categories", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.back_button),
                contentDescription = "Previous Category",
                modifier = Modifier.size(24.dp).clickable {
                    if (currentCategoryIndex > 0) currentCategoryIndex--
                    selectedCategory = categories[currentCategoryIndex].first
                }
            )

            LazyRow(
                modifier = Modifier.weight(1f)
            ) {
                items(categories) { (category, imageResId) ->
                    Column(
                        modifier = Modifier
                            .clickable {
                                selectedCategory = category
                            }
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = category,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = category,
                            fontSize = 14.sp,
                            color = if (selectedCategory == category) Color.Blue else Color.Black
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.forward_arrow),
                contentDescription = "Next Category",
                modifier = Modifier.size(24.dp).clickable {
                    if (currentCategoryIndex < categories.size - 1) currentCategoryIndex++
                    selectedCategory = categories[currentCategoryIndex].first
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    AnalyticsScreen(navController = NavController(LocalContext.current))
}
