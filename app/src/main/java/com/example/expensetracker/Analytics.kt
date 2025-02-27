package com.example.expensetracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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

            ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back_button),
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Analytics",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start, // Center the text horizontally
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 60.dp) // Ensure the text takes full width
            )
        }
        Spacer(modifier = Modifier.height(20.dp))


        // Balance Card

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
                                colorResource(id = R.color.gradient_start), // Top color from colors.xml
                                colorResource(id = R.color.gradient_end) // Bottom color from colors.xml
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
                            painter = painterResource(id = R.drawable.increase), // Use your arrow icon
                            contentDescription = "Growth Indicator",
//                                tint = colorResource(id = R.color.green_accent), // Use color from colors.xml
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
                                color = colorResource(id = R.color.white),
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
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
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
                Image(
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

        // Graph Placeholder (Replace with actual Graph logic)
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp)
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
            Image(
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

            Image(
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

