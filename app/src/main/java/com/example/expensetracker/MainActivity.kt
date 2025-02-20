package com.example.expensetracker

import ProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import androidx.lint.kotlin.metadata.Visibility
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                val navController = rememberNavController() // Create NavController
                NavHost(
                    navController = navController,
                    startDestination = "splash-screen"
                ) {
                    composable("splash-screen") {
                        BackgroundImage(navController)
                    }
                    composable("signup-screen") {
                        SignupScreen(navController)
                    }
                    composable("login-screen") {
                        LoginScreen(navController)
                    }
                    composable("addphoto-screen") {
                        AddPhoto(navController)
                    }
                    composable("forgot-pwd")
                    {
                        ForgotPasswordPage(navController)
                    }
                    composable("enter-phone-number"){
                        OTPResetPage(navController)
                    }

                    composable("home-screen") {
                        HomeScreen(navController)
                    }
                    composable("transaction-history") {
                        TransactionHistoryScreen(navController)
                    }
                    composable("expenses-screen") {
                        ExpensesScreen(navController)
                    }
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun WhiteButtonPreview() {
    ExpenseTrackerTheme {
        WhiteButton(title = "Log in with Google")
    }
}

@Preview(showBackground = true)
@Composable
fun WhiteButtonWithStrokePreview() {
    ExpenseTrackerTheme {
        WhiteButtonWithStroke(title = "Maybe Later")
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    ExpenseTrackerTheme {
        BackgroundImage(navController = rememberNavController())
    }
}

@Composable
fun WhiteButtonWithDarkText(onClick: () -> Unit = {}, title: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // Button background color
            contentColor = Color.Black // Default content color
        ),
        shape = RoundedCornerShape(70.dp), // Rounded corners
        modifier = Modifier
            .padding(16.dp)
            .height(70.dp)
            .width(342.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(70.dp)) // Add a light grey border
    ) {
        Text(
            text = title,
            color = Color.Black, // Explicitly set text color
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun WhiteButtonWithStroke(onClick: () -> Unit = {}, title: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // Button background color
            contentColor = Color.LightGray // Default content color
        ),
        shape = RoundedCornerShape(70.dp), // Rounded corners
        modifier = Modifier
            .padding(16.dp)
            .height(70.dp)
            .width(342.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(70.dp)) // Add a light grey border
    ) {
            Text(
                text = title,
                color = Color.LightGray, // Explicitly set text color
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
    }
}

@Composable
fun WhiteButton(onClick: () -> Unit = {}, title: String) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, // Button background color
            contentColor = Color.White // Default content color
        ),
        shape = RoundedCornerShape(70.dp), // Rounded corners
        modifier = Modifier
//            .padding(16.dp)
            .height(70.dp)
            .width(342.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // Align icon and text vertically
            horizontalArrangement = Arrangement.Center // Center content horizontally
        ) {
            // Add the Google icon
            Image(
                painter = painterResource(id = R.drawable.google_icon_image), // Replace with your Google icon resource
                contentDescription = "Google Icon",
                modifier = Modifier
                    .size(28.dp) // Set the size of the icon
                    .padding(end = 8.dp) // Add some spacing between the icon and text
            )
            // Add the text
            Text(
                text = title,
                color = Color.Black, // Explicitly set text color
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BackgroundImage(navController: NavController) {
    val image = painterResource(R.drawable.background_image)


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6F) // 60% of screen height
        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
//            .padding(top = 28.dp)
            .background(colorResource(id = R.color.HomeColor))
            .padding(top = 40.dp)
            .padding(end = 20.dp)
            .padding(start = 20.dp), // Adds padding inside the column// Adds padding inside the column
        verticalArrangement = Arrangement.Top // Centers content vertically
    ) {
        // Title
        Text(
            text = "Always Track\nYour Expenses",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            lineHeight = 32.sp,
            textAlign = TextAlign.Left, // Align the text to the left
            modifier = Modifier.padding(start = 20.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Subtitle
        Text(
            text = "Now you can track expenses records on\nyour phone",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        Column(
            modifier = Modifier.fillMaxSize(), // Ensures the Column takes up the full screen
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            FilledButton(title = "Get Started", destination = "signup-screen", navController = navController)
            // Login Prompt
            Spacer(modifier = Modifier.height(16.dp))
            TextForSigningUpOrLoginIn(navController = navController)
        }

    }
    }
}

@Composable
fun FilledButton( navController: NavController,
                  onClick: () -> Unit = {},
                  title: String,
                  destination: String
)
{
    Button(
onClick = {
    onClick() // Call any additional onClick logic
    navController.navigate(destination) // Navigate to the specified destination
},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF336B40), // Custom green button color
            contentColor = Color.White // White text color
        ),
        shape = RoundedCornerShape(70.dp), // Optional: rounded corners
        modifier = Modifier
//            .padding(16.dp)
            .height(65.dp)
            .width(332.dp)
    ) {
        Text(
            text = title,
            color = Color.White, // Explicitly setting text color to white
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TextForSigningUpOrLoginInPreview() {
    ExpenseTrackerTheme {
        val navController = rememberNavController() // Create a dummy NavController for preview
        TextForSigningUpOrLoginIn(navController = navController)
    }
}



@Composable
fun TextForSigningUpOrLoginIn(navController: NavController){
    Box(
        contentAlignment = Alignment.Center// Centers the content both horizontally and vertically
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Have an account?",
                fontSize = 14.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Login",
                fontSize = 14.sp,
                color = colorResource(id = R.color.LoginColor), // Replace 'your_color_name' with the name of your color in colors.xml
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("login-screen") // Navigate to login screen
                }
            )
        }
    }
}


@Preview (showBackground = true)
@Composable
fun SignUpScreenPreview() {
    ExpenseTrackerTheme {
        SignupScreen(navController = rememberNavController())
    }
}

@Composable
fun SignupScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
//            .padding(top = 100.dp)
            .padding(
                start = 30.dp, // Left padding
                end = 30.dp,   // Right padding
                top = 100.dp,  // Top padding
                bottom = 50.dp // Bottom padding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Sign Up",
            style = TextStyle(color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )
//        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please fill in your details to proceed",
            style = TextStyle(fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Username Field
            CustomUnderlineTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username"
            )

            // Email Field
            CustomUnderlineTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email"
            )

            // Password Field
            CustomUnderlineTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true
            )

            // Confirm Password Field
            CustomUnderlineTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                isPassword = true
            )

        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            FilledButton(title = "Continue", destination = "addphoto-screen", navController = navController)
            // Login Prompt
            Spacer(modifier = Modifier.height(16.dp))
            TextForSigningUpOrLoginIn(navController = navController)
        }
    }
}

@Preview (showBackground = true)
@Composable
fun LoginScreenPreview() {
    ExpenseTrackerTheme {
        LoginScreen(navController = rememberNavController())
    }
}
@Preview (showBackground = true)
@Composable
fun AddPhotoPreview() {
    ExpenseTrackerTheme {
        AddPhoto(navController = rememberNavController())
    }
}

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val image = painterResource(R.drawable.waving_hand)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
//            .padding(top = 100.dp)
            .padding(
                start = 30.dp, // Left padding
                end = 30.dp,   // Right padding
                top = 100.dp,  // Top padding
                bottom = 50.dp // Bottom padding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Top
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Welcome Back !",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 4.dp) // Removed bottom padding to align with the image
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(34.dp) // Matches the font size of the text
                    .padding(bottom = 4.dp) // Adjusted bottom padding to align with the text
            )
        }
//

        Text(
            text = "Please fill in your details to proceed",
            style = TextStyle(fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Email Field
            CustomUnderlineTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email"
            )

            // Password Field
            CustomUnderlineTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true
            )




        }
        Text(
            text = "Forgot password?",
            style = TextStyle(
                color = colorResource(id = R.color.ForgotPasswordColor),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,

                ),
            modifier = Modifier
                .align(Alignment.End)// Adjust top padding to align with the image
               .clickable {
                navController.navigate("forgot-pwd") // Navigate to login screen
            }
        )
        Column(
          verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
             .padding(top = 40.dp)

        ){
            FilledButton(title = "Log in", destination ="home-screen", navController = navController)
         Spacer(modifier = Modifier.height(10.dp)) // Add a small spacer for minimal space
            WhiteButton(title = "Log in with Google")
            // Login Prompt

        }
        Spacer(modifier = Modifier.height(16.dp))
        TextForSignup(navController = navController)
    }
}

@Composable
fun TextForSignup(navController: NavController)  {Box(
contentAlignment = Alignment.Center// Centers the content both horizontally and vertically
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don't have an account?",
            fontSize = 14.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Signup",
            fontSize = 14.sp,
            color = colorResource(id = R.color.LoginColor), // Replace 'your_color_name' with the name of your color in colors.xml
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate("signup-screen") // Navigate to login screen
            }
        )
    }
}
}


// Custom reusable text field with an underline only
@Composable
fun CustomUnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(Color.White),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .padding(bottom = 15.dp)
                .padding(end = if (isPassword) 40.dp else 0.dp)
        ) { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth()
//                    .padding(bottom = 10.dp)
            ) {
                if (value.isEmpty() && !isFocused) {
                    Text(
                        text = placeholder,
                        style = TextStyle(color = Color.White.copy(alpha = 0.5f), fontSize = 16.sp)
                    )
                }
                innerTextField()
            }
        }

        // Underline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White)
                .align(Alignment.BottomStart)
        )

        // Password Visibility Toggle
        if (isPassword) {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Image(
                    painter = painterResource(if (isPasswordVisible) R.drawable.view_password_3__1_ else R.drawable.view_password_negative),
                    contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPhoto(navController: NavController) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    val image = painterResource(R.drawable.back_button)
    val photographImage = painterResource(R.drawable.photograph_image)
    val humanProfile = painterResource(R.drawable.human_profile)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Icon(
                    painter = image,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // "Add a photo" with camera icon
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add a photo",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = photographImage,
                    contentDescription = "Camera Icon",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Profile Placeholder Image
            Image(
                painter = humanProfile,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Button to Open Bottom Sheet
            FilledButton(
                onClick = { showBottomSheet = true },
                title = "Choose a Photo",
                destination = "addphoto-screen",
                navController = navController
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Maybe Later Button
            WhiteButtonWithStroke(
                onClick = { navController.popBackStack() },
                title = "Maybe Later"
            )
        }
    }

    // Bottom Sheet for Photo Options
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WhiteButtonWithDarkText(
                    onClick = { /* Handle Take Photo */ },
                    title = "Take Photo"
                )
                Divider()
                WhiteButtonWithDarkText(
                    onClick = { /* Handle Gallery Selection */ },
                    title = "Add from Gallery"
                )
            }
        }
    }
}











