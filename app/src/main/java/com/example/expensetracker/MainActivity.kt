package com.example.expensetracker

import ProfileScreen
import android.content.res.Configuration
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
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
//import androidx.lint.kotlin.metadata.Visibility
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.walletapp.TransactionHistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false) // Allows full edge-to-edge content
        setContent {
            val darkTheme = isSystemInDarkTheme() // Check system theme
            ExpenseTrackerTheme(darkTheme = false) {
               Surface(
                   modifier = Modifier.fillMaxSize(),
                   color = MaterialTheme.colorScheme.background
               ){

                   // Adjust status bar based on theme
                   window.statusBarColor = if (darkTheme) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                   WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !darkTheme
                   MyAppNavigation()
                }
            }
        }

    }
}


@Composable
fun MyAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash-screen") {
        composable("splash-screen") {
            TeaserScreen(navController)
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
        composable("account-page"){
            ProfileScreen(navController)
        }
        composable("reset-pwd")
        {
            ResetPassword(navController)
        }
        composable("enter-code")
        {
            ConfirmNumber(navController)
        }
        composable("enter-phone-number") {
            OTPResetPage(navController)
        }

        composable("home-screen") {
            HomeScreen(navController)
        }
        composable("add-expense-screen") {
            AddExpenseScreen(navController)
        }
        composable("add-income-screen"){
            AddIncomeScreen(navController)
        }

        composable("transaction-history") {
            TransactionHistoryScreen(navController)
        }
        composable("expenses-screen") {
            ExpensesScreen(navController)
        }
        composable("analytics-screen"){
            AnalyticsScreen(navController)
        }

        composable("edit-expense-screen/{expenseId}") { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")
            val expense = getExpenseById(expenseId) // Fetch expense based on ID
            if (expense != null) {
                EditExpenseScreen(navController, expense)
            }
        }
        composable("my-account-page")
        {
            ProfileScreen(navController)
        }
        composable("change-details-screen"){
            ChangeDetailsScreen(navController)
        }
        composable("change-password-details"){
            ChangePasswordScreen(navController)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    ExpenseTrackerTheme {
        TeaserScreen(navController = rememberNavController())
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
            .height(65.dp)
            .width(332.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(70.dp)) // Add a light grey border
    ) {
            Text(
                text = title,
                color = Color.DarkGray, // Explicitly set text color
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
            .height(65.dp)
            .fillMaxWidth(0.9f) // Flexible width
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
fun TeaserScreen(navController: NavController) {
    val image = painterResource(R.drawable.background_image)
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Image Section
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isPortrait) 0.6f else 0.4f) // Less height in landscape
        )

        // Bottom Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isPortrait) 0.4f else 0.6f) // More space for text in landscape
                .background(colorResource(id = R.color.HomeColor))
                .padding(start = 20.dp, top = if (isPortrait) 40.dp else 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Title
            Text(
                text = "Always Track\nYour Expenses",
                style = MaterialTheme.typography.titleLarge.copy( // 🔥 Uses your theme
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "Now you can track expenses records on\nyour phone",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White) // 🔥 Uses your theme
            )

            Spacer(modifier = Modifier.height(if (isPortrait) 30.dp else 20.dp))

            // Button and Login Prompt
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilledButton(title = "Get Started", destination = "signup-screen", navController = navController)
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
                  destination: String,
                  enabled: Boolean = true // Add this parameter with a default value
)
{
    Button(
onClick = {
    onClick()// Call any additional onClick logic
    navController.navigate(destination) // Navigate to the specified destination
},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF336B40), // Custom green button color
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF336B40).copy(alpha = 0.67f) // 67% opacity for disabled button
        ),

                enabled = enabled, // Use the enabled parameter

        shape = RoundedCornerShape(70.dp), // Optional: rounded corners
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth(0.9f) // Flexible width
    ) {
        Text(
            text = title,
            color = Color.White, // Explicitly setting text color to white
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun AnalyticsScreenPreview() {
//    ExpenseTrackerTheme {
//        val navController = rememberNavController() // Create a dummy NavController for preview
//        AnalyticsScreen(navController = navController)
//    }
//}



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
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val isLandscape = screenWidth > screenHeight

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Track if the user has interacted with each field
    var usernameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var confirmPasswordTouched by remember { mutableStateOf(false) }

    val isUsernameInvalid = usernameTouched && username.isEmpty()
    val isEmailInvalid = emailTouched && (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
    val isPasswordInvalid = passwordTouched && (password.isEmpty() || password.length < 6)
    val isConfirmPasswordInvalid = confirmPasswordTouched && (confirmPassword.isEmpty() || confirmPassword != password)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = minOf(500.dp, screenWidth * 0.8f))
            .background(colorResource(id = R.color.HomeColor))
            .padding(horizontal = if (isLandscape) 50.dp else 30.dp, vertical = if (isLandscape) 20.dp else 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Please fill in your details to proceed",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Username Field
                CustomUnderlineTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameTouched = true
                    },
                    placeholder = "Username",
                    isError = isUsernameInvalid
                )
                if (isUsernameInvalid) {
                    Text(
                        text = "Username cannot be empty",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Email Field
                CustomUnderlineTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailTouched = true
                    },
                    placeholder = "Email",
                    isError = isEmailInvalid
                )
                if (isEmailInvalid) {
                    Text(
                        text = "Enter a valid email",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Password Field
                CustomUnderlineTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordTouched = true
                    },
                    placeholder = "Password",
                    isPassword = true,
                    isError = isPasswordInvalid
                )
                if (isPasswordInvalid) {
                    Text(
                        text = "Password must be at least 6 characters",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Confirm Password Field
                CustomUnderlineTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordTouched = true
                    },
                    placeholder = "Confirm Password",
                    isPassword = true,
                    isError = isConfirmPasswordInvalid
                )
                if (isConfirmPasswordInvalid) {
                    Text(
                        text = "Passwords do not match",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ✅ Disable button when invalid
                FilledButton(
                    title = "Continue",
                    destination = "addphoto-screen",
                    navController = navController,
                    enabled = !(isUsernameInvalid || isEmailInvalid || isPasswordInvalid || isConfirmPasswordInvalid),
                    onClick = {
                        navController.navigate("addphoto-screen")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextForSigningUpOrLoginIn(navController = navController)
            }
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

    // Track if the user has interacted with each field
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    val image = painterResource(R.drawable.waving_hand)
    val context = LocalConfiguration.current
    val screenWidth = context.screenWidthDp.dp

    val isEmailInvalid = emailTouched && (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
    val isPasswordInvalid = passwordTouched && (password.isEmpty() || password.length < 6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
            .padding(
                start = 30.dp,
                end = 30.dp,
                top = 100.dp,
                bottom = 50.dp
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
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(34.dp)
            )
        }

        Text(
            text = "Please fill in your details to proceed",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .widthIn(max = minOf(500.dp, screenWidth * 0.8f))
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email Field
            CustomUnderlineTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true // Mark email as touched
                },
                placeholder = "Email",
                isError = isEmailInvalid
            )
            if (isEmailInvalid) {
                Text(
                    text = "Enter a valid email",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Password Field
            CustomUnderlineTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true // Mark password as touched
                },
                placeholder = "Password",
                isPassword = true,
                isError = isPasswordInvalid
            )
            if (isPasswordInvalid) {
                Text(
                    text = "Password must be at least 6 characters",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Text(
            text = "Forgot password?",
            style = MaterialTheme.typography.labelSmall.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    navController.navigate("forgot-pwd")
                }
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            // ✅ Disable button when invalid
            FilledButton(
                title = "Log in",
                destination = "home-screen",
                navController = navController,
                enabled = !(isEmailInvalid || isPasswordInvalid),
                onClick = {
                    navController.navigate("home-screen")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            WhiteButton(title = "Log in with Google")
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
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "" // New parameter
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
            ) {
                if (value.isEmpty() && !isFocused) {
                    Text(
                        text = placeholder,
                        style = TextStyle(color = Color.White.copy(alpha = 0.5f), fontSize = 16.sp)
                    )
                }
                innerTextField()
            }

            // Display custom error text if isError is true
            if (isError && errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
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
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val image = painterResource(R.drawable.back_button)
    val photographImage = painterResource(R.drawable.photograph_image)
    val humanProfile = painterResource(R.drawable.human_profile)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Bottom Sheet
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
                    onClick = {
                        showBottomSheet = false
                        // Handle Take Photo action
                    },
                    title = "Take Photo"
                )
                Divider()
                WhiteButtonWithDarkText(
                    onClick = {
                        showBottomSheet = false
                        // Handle Gallery Selection action
                    },
                    title = "Add from Gallery"
                )
            }
        }
    }

    // Main Screen Content
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 46.dp, start = 16.dp),
            ) {
                Icon(
                    painter = image,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title with Camera Icon
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add a photo",
                    style = MaterialTheme.typography.headlineLarge.copy(color = Color.Black),
                    modifier = Modifier.widthIn(max = minOf(500.dp, screenWidth * 0.8f))
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
                    .size(140.dp)
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
                onClick = { navController.navigate("home-screen") },
                title = "Maybe Later"
            )
        }
    }
}












