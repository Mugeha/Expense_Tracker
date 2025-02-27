package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    var balanceVisible by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("This Week") }
    val image = painterResource(id = R.drawable.human_profile)


// State to track scroll offset
    val scrollState = rememberScrollState()
    var bottomBarVisible by remember { mutableStateOf(true) }

    // Logic to hide/show bottom bar based on scroll direction
    LaunchedEffect(scrollState.value) {
        if (scrollState.value > 0) {
            bottomBarVisible = true
        } else {
            bottomBarVisible = true
        }
    }

    Scaffold(
      bottomBar = {
            if (bottomBarVisible) {
                BottomNavigationBar(navController, selectedItem = "Home")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
//                .background(color = Color.White)

        ) {
            // User Profile
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Image(
                    painter = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clickable {
                            // Navigate to another page when the image is clicked
                            navController.navigate("account-page")
                        }
                )
                Text(
                    text = "Welcome, \nMugeha Jackline",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Image(
//                                painter = painterResource(id = R.drawable.increase), // Use your arrow icon
//                                contentDescription = "Growth Indicator",
////                                tint = colorResource(id = R.color.green_accent), // Use color from colors.xml
//                                modifier = Modifier.size(24.dp)
//                            )
//
//                            Spacer(modifier = Modifier.width(4.dp))
//
//                            Row {
//                                Text(
//                                    text = "+35% ",
//                                    color = colorResource(id = R.color.increaseColor),
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.Medium
//                                )
//                                Text(
//                                    text = "Than Last Month",
//                                    color = colorResource(id = R.color.white),
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.Medium
//                                )
//                            }
//                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

           DashboardScreen(navController = navController)

            Spacer(modifier = Modifier.height(20.dp))

            // Summary Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 26.dp), // Add padding to the start and end
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {
                Text(
                    text = "Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Dropdown with "This Week" text and chevron icon
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { expanded = true } // Open dropdown when clicked
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedPeriod,
                            color = Color.DarkGray, // Dark grey color for the text
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 4.dp) // Add spacing between text and icon
                        )
                        Image(
                            painter = painterResource(R.drawable.down_chevron),
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(14.dp) // Adjust size of the chevron icon
                        )
                    }

                    // Dropdown menu
                    androidx.compose.material3.DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White) // White background for the dropdown
                    ) {
                        listOf("This Week", "This Month", "This Year").forEach { item ->
                            androidx.compose.material3.DropdownMenuItem(
                                onClick = {
                                    selectedPeriod = item
                                    expanded = false
                                },
                                text = {
                                    Text(
                                        text = item,
                                        color = Color.DarkGray, // Dark grey text color
                                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                                    )
                                },
                                modifier = Modifier
//                                    .background(Color(0xFFa3a8a4)) // Darker grey background for each item
                                    .padding(horizontal = 14.dp, vertical = 4.dp)
                                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)) // Light gray border
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transaction List (Scrollable)
            TransactionList()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavController(LocalContext.current))
}

@Composable
fun DashboardButton(
    imageResource: Int,
    text: String,
    navController: NavController, // Add navController parameter
    destination: String, // Add destination route parameter
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
//            .size(110.dp)
            // Adjust size as needed
            .height(150.dp)
            .width(125.dp)
            .padding(10.dp)
            .clip(RoundedCornerShape(16.dp)) // Clip for rounded corners
          .background(backgroundColor) // Set background color
            .clickable(onClick = onClick) // Make it clickable
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),// Optional: Add a border
        contentAlignment = Alignment.Center // Center content inside Box
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = text,
                modifier = Modifier.size(38.dp), // Adjust icon size
//                tint = Color.White // Icon color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center, // Ensures multi-line text is centered
                modifier = Modifier.fillMaxWidth() // Helps center the text properly

            )
        }
    }
}


@Composable
fun DashboardScreen(navController: NavController) {
    Row(
        modifier = Modifier
           .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly, // Distribute buttons evenly
        verticalAlignment = Alignment.CenterVertically
    ) {
        DashboardButton(
            imageResource = R.drawable.money, // Replace with your income icon
            text = "Add \nIncome",
            navController = navController, // Pass navController
            destination = "add-income-screen", // Replace with your income screen route
            backgroundColor = colorResource(id = R.color.HomeColor), // Example: Green color
            onClick = { navController.navigate("add-income-screen")}
        )
        DashboardButton(
            imageResource = R.drawable.money__1_, // Replace with your expenses icon
            text = "Add \nExpenses",
            navController = navController, // Pass navController
            destination = "add-expense-screen", // Replace with your income screen route
            backgroundColor = colorResource(id = R.color.HomeColor), // Example: Red color
            onClick = { navController.navigate("add-expense-screen") }
        )
        DashboardButton(
            imageResource = R.drawable.bar_chart, // Replace with your analytics icon
            text = "See \nAnalytics",
            navController = navController, // Pass navController
            destination = "see-analytics-screen", // Replace with your income screen route
            backgroundColor = colorResource(id = R.color.HomeColor),// Example: Blue color
            onClick = { navController.navigate("analytics-screen") }
        )
    }
}



@Composable
fun TransactionItem(title: String, subtitle: String, amount: String, isIncome: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9)) // Light gray background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Transaction Icon
            Image(
                painter = painterResource(id = if (isIncome) R.drawable.money else R.drawable.expenses),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Transaction Details
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = subtitle, fontSize = 14.sp, color = Color.Black.copy(alpha = 0.6f))
            }

            // Transaction Amount
            Text(
                text = amount,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isIncome) Color(0xFF30872D) else Color(0xFF8A3E3E) // Green for income, Red for expenses
            )
        }
    }
}

@Composable
fun TransactionList() {
    Column(modifier = Modifier.fillMaxWidth()) { // Changed to fillMaxWidth
        // First TransactionItem
        TransactionItem(
            title = "Income",
            subtitle = "Freelance Project",
            amount = "+$450",
            isIncome = true
        )

        // Second TransactionItem
        TransactionItem(
            title = "Expenses",
            subtitle = "Dribbble Pro Subscription",
            amount = "-$15",
            isIncome = false
        )
    }
}

