package com.example.expensetracker

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.expensetracker.data.remote.SessionManager
import com.example.expensetracker.viewModel.PhotoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, context: Context, photoViewModel: PhotoViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var balanceVisible by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("This Week") }

    // ✅ Fetch user session data directly from SessionManager
    val sessionManager = remember { SessionManager(context) }
    val username = remember { sessionManager.getUsername() }  // Fetch stored username
    val storedProfileImage = remember { sessionManager.getProfileImage() } // Fetch stored profile image URL

    // ✅ Observe image URI from ViewModel
    val imageUri by photoViewModel.profileImageUri.observeAsState()

    // ✅ Use locally stored image if available, otherwise use the image from the backend
    val finalProfileImage = imageUri ?: storedProfileImage

    // ✅ Load profile image only when the screen is launched
    LaunchedEffect(Unit) {
        photoViewModel.loadProfileImage() // ✅ No arguments needed
    }


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, selectedItem = "Home")
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // ✅ User Profile Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // ✅ Display Profile Image (From Local Cache or Backend)
                Image(
                    painter = finalProfileImage?.let { rememberAsyncImagePainter(it) }
                        ?: painterResource(R.drawable.human_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate("account-page")
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))

                // ✅ Display Username (From SessionManager)
                Text(
                    text = "Welcome, \n${username ?: "User"}", // Default to "User" if username is null
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Balance Card
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

                            IconButton(onClick = { balanceVisible = !balanceVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (balanceVisible) R.drawable.view_password_3__1_
                                        else R.drawable.view_password_negative
                                    ),
                                    contentDescription = if (balanceVisible) "Hide Balance" else "Show Balance",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // ✅ Dashboard Section
            DashboardScreen(navController = navController)

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ Summary Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 26.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Dropdown for Period Selection
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable { expanded = true }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = selectedPeriod,
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Image(
                            painter = painterResource(R.drawable.down_chevron),
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        listOf("This Week", "This Month", "This Year").forEach { item ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedPeriod = item
                                    expanded = false
                                },
                                text = {
                                    Text(
                                        text = item,
                                        color = Color.DarkGray,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Transaction List (Scrollable)
            TransactionList()
        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen(navController = NavController(LocalContext.current))
//}

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
            amount = "+KSH450",
            isIncome = true
        )

        // Second TransactionItem
        TransactionItem(
            title = "Expenses",
            subtitle = "Dribbble Pro Subscription",
            amount = "-KSH15",
            isIncome = false
        )
    }
}

