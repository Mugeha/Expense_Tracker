package com.example.expensetracker

import AuthRepository
import PhotoViewModel
import PhotoViewModelFactory
import ProfileScreen
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import coil.compose.rememberAsyncImagePainter
import androidx.activity.ComponentActivity
import com.google.accompanist.permissions.rememberPermissionState
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.api.ApiService
import com.example.expensetracker.data.remote.SessionManager
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.walletapp.TransactionHistoryScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.window.Dialog
import com.example.expensetracker.viewModel.AuthViewModel
import com.example.expensetracker.viewModel.AuthViewModelFactory
import com.example.expensetracker.viewModel.SignupSharedViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.delay



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
            TeaserScreen(navController, context = LocalContext.current)
        }
        composable("signup-screen") {
            SignupScreen(navController, context = LocalContext.current)
        }
        composable("login-screen") {
            LoginScreen(navController, context = LocalContext.current)
        }

        composable("addphoto-screen") { backStackEntry ->
            val context = LocalContext.current
            val application = context.applicationContext as Application

            val sessionManager = remember { SessionManager(context) }
            val apiService = remember { ApiService.create(sessionManager) }
            val authRepository = remember { AuthRepository(apiService, sessionManager) }

            val photoViewModel: PhotoViewModel = viewModel(
                factory = PhotoViewModelFactory(application, context)
            )

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository)
            )

            val signupSharedViewModel: SignupSharedViewModel = viewModel(backStackEntry)

            AddPhoto(
                navController = navController,
                photoViewModel = photoViewModel,
                authViewModel = authViewModel,
                signupSharedViewModel = signupSharedViewModel
            )
        }


        composable("forgot-pwd")
        {
            ForgotPasswordPage(navController)
        }
        composable("account-page") { navBackStackEntry ->
            val context = LocalContext.current
            val application = context.applicationContext as Application

            val sessionManager = remember { SessionManager(context) }
            val apiService = remember { ApiService.create(sessionManager) }
            val authRepository = remember { AuthRepository(apiService, sessionManager) }

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository),
                viewModelStoreOwner = navBackStackEntry
            )

            val photoViewModel: PhotoViewModel = viewModel(
                factory = PhotoViewModelFactory(application, context),
                viewModelStoreOwner = navBackStackEntry
            )

            ProfileScreen(
                navController = navController,
                photoViewModel = photoViewModel,
                authViewModel = authViewModel,
                context = context
            )
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
            val context = LocalContext.current
            val application = context.applicationContext as Application

            val photoViewModel: PhotoViewModel = viewModel(
                factory = PhotoViewModelFactory(application, context)
            )

            HomeScreen(
                navController = navController,
                context = context,
                photoViewModel = photoViewModel
            )
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
            val expense = getExpenseById(expenseId) // Fetch the actual expense

            if (expense != null) {
                EditExpenseScreen(navController, expense)
            } else {
                Text("Expense not found") // Handle case where ID is invalid
            }
        }


        composable("edit-expense-screen/{expenseId}") { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getString("expenseId")
            val expense = getExpenseById(expenseId) // Fetch expense based on ID
            if (expense != null) {
                EditExpenseScreen(navController, expense)
            }
        }

        composable("change-details-screen"){
            ChangeDetailsScreen(navController)
        }
        composable("change-password-details"){
            ChangePasswordScreen(navController)
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun HomePagePreview() {
//    ExpenseTrackerTheme {
//        TeaserScreen(navController = rememberNavController())
//    }
//}

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
fun TeaserScreen(navController: NavController, context: Context) {
    val sessionManager = remember { SessionManager(context) }

    // ✅ Auto-navigation logic
    LaunchedEffect(Unit) {
        delay(1000) // Optional splash delay
        if (sessionManager.isLoggedIn()) {
            navController.popBackStack()
            navController.navigate("home-screen")
        }
    }

    val image = painterResource(R.drawable.background_image)
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Image Section
        Image(
            painter = image,
            contentDescription = "Teaser Background",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isPortrait) 0.6f else 0.4f)
        )

        // Bottom Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isPortrait) 0.4f else 0.6f)
                .background(colorResource(id = R.color.HomeColor))
                .padding(start = 20.dp, top = if (isPortrait) 40.dp else 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Always Track\nYour Expenses",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Now you can track expense records\non your phone",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
            )

            Spacer(modifier = Modifier.height(if (isPortrait) 30.dp else 20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilledButton(
                    title = "Get Started",
                    onClick = {
                        navController.popBackStack()
                        navController.navigate("signup-screen")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextForSigningUpOrLoginIn(navController = navController)
            }
        }
    }
}


@Composable
fun FilledButton(
    title: String,
    enabled: Boolean = true, // Keep it for disabling button when needed
    onClick: () -> Unit // Add this to handle navigation
) {
    Button(
        onClick = onClick, // Calls whatever logic you pass in
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF336B40),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF336B40).copy(alpha = 0.67f)
        ),
        enabled = enabled,
        shape = RoundedCornerShape(70.dp),
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth(0.9f)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
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


@Composable
fun SignupScreen(
    navController: NavController,
    context: Context,
) {
    val sessionManager = SessionManager(context)
    val apiService = ApiService.create(sessionManager)
    val authRepository = AuthRepository(apiService, sessionManager)
    val signupSharedViewModel: SignupSharedViewModel = viewModel()


    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))


    val signupResult by authViewModel.signupResult.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()


    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }
    var confirmPasswordTouched by remember { mutableStateOf(false) }

    val isUsernameInvalid = usernameTouched && (username.isEmpty() || username.length < 3)
    val isEmailInvalid = emailTouched && (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
    val isPasswordInvalid = passwordTouched && (password.isEmpty() || password.length < 6)
    val isConfirmPasswordInvalid = confirmPasswordTouched && (confirmPassword.isEmpty() || confirmPassword != password)

    LaunchedEffect(signupResult) {
        signupResult?.let { result ->
            result.onSuccess { userResponse ->
                Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()

                // ✅ Save session info before navigating
                sessionManager.saveUserSession(
                    token = userResponse.token,
                    username = userResponse.username,
                    profileImage = "" // placeholder, photo comes later
                )
                sessionManager.saveEmail(userResponse.email)

                signupSharedViewModel.setUsername(username)
                signupSharedViewModel.setEmail(email)
                signupSharedViewModel.setPassword(password)

                navController.navigate("addphoto-screen") {
                    popUpTo("signup-screen") { inclusive = true }
                }

            }.onFailure { error ->
                Toast.makeText(context, error.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
            }
        }
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
            .padding(horizontal = 30.dp, vertical = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            LoadingDialog()
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sign Up", style = MaterialTheme.typography.headlineLarge.copy(color = Color.White))
            Text(
                "Please fill in your details to proceed",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f))
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomUnderlineTextField(
                value = username,
                onValueChange = { username = it; usernameTouched = true },
                placeholder = "Username",
                isError = isUsernameInvalid
            )
            if (isUsernameInvalid) Text("Username must be at least 3 characters", color = Color.Red)

            CustomUnderlineTextField(
                value = email,
                onValueChange = { email = it; emailTouched = true },
                placeholder = "Email",
                isError = isEmailInvalid
            )
            if (isEmailInvalid) Text("Enter a valid email", color = Color.Red)

            CustomUnderlineTextField(
                value = password,
                onValueChange = { password = it; passwordTouched = true },
                placeholder = "Password",
                isPassword = true,
                isError = isPasswordInvalid
            )
            if (isPasswordInvalid) Text("Password must be at least 6 characters", color = Color.Red)

            CustomUnderlineTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; confirmPasswordTouched = true },
                placeholder = "Confirm Password",
                isPassword = true,
                isError = isConfirmPasswordInvalid
            )
            if (isConfirmPasswordInvalid) Text("Passwords do not match", color = Color.Red)

            Spacer(modifier = Modifier.height(30.dp))

            FilledButton(
                title = if (isLoading) "Signing Up..." else "Continue",
                enabled = !isLoading && !isUsernameInvalid && !isEmailInvalid && !isPasswordInvalid && !isConfirmPasswordInvalid,
                onClick = {
                    authViewModel.signup(username, email, password)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            TextForSigningUpOrLoginIn(navController = navController)
        }
    }
}


@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = { /* Disable dismiss by back press */ }) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}


@Composable
fun LoginScreen(navController: NavController, context: Context) {
    val sessionManager = SessionManager(context)
    val apiService = ApiService.create(sessionManager)
    val authRepository = AuthRepository(apiService, sessionManager)

    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    val isEmailInvalid = emailTouched && (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
    val isPasswordInvalid = passwordTouched && (password.isEmpty() || password.length < 6)

    val loginResult by authViewModel.loginResult.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()


    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            result.onSuccess { response ->
                // ✅ Save everything to session
                sessionManager.saveUserSession(
                    token = response.token,
                    username = response.username,
                    profileImage = response.profileImage
                )
                sessionManager.saveEmail(response.email)

                // ✅ Navigate to home screen
                navController.navigate("home-screen") {
                    popUpTo("login-screen") { inclusive = true }
                }
            }.onFailure { error ->
                Toast.makeText(context, error.message ?: "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }




    val image = painterResource(R.drawable.waving_hand)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // your UI (unchanged) ...

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White)
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
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                Text("Enter a valid email", color = Color.Red)
            }

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
                Text("Password must be at least 6 characters", color = Color.Red)
            }
        }

        Text(
            text = "Forgot password?",
            style = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
            modifier = Modifier
                .align(Alignment.End)
                .clickable { navController.navigate("forgot-pwd") }
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            FilledButton(
                title = if (isLoading) "Logging In..." else "Log in",
                enabled = !isLoading && !isEmailInvalid && !isPasswordInvalid,
                onClick = {
                    authViewModel.login(email, password)
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
            style = MaterialTheme.typography.bodyLarge.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
            color = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Signup",
            style = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddPhoto(
    navController: NavController,
    photoViewModel: PhotoViewModel,
    authViewModel: AuthViewModel,
    signupSharedViewModel: SignupSharedViewModel
) {
    val email = signupSharedViewModel.email
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val profileImageUri by photoViewModel.profileImageUri.observeAsState()
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        photoViewModel.loadProfileImage()
    }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val takePhotoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempImageUri != null) {
            photoViewModel.saveProfileImage(tempImageUri!!)
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            photoViewModel.saveProfileImage(it)
        }
    }

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
                        if (cameraPermissionState.status.isGranted) {
                            val newUri = photoViewModel.createImageUri()
                            if (newUri != null) {
                                tempImageUri = newUri
                                takePhotoLauncher.launch(newUri)
                            }
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    title = "Take Photo"
                )
                Divider()
                WhiteButtonWithDarkText(
                    onClick = {
                        showBottomSheet = false
                        pickImageLauncher.launch("image/*")
                    },
                    title = "Add from Gallery"
                )
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 46.dp, start = 16.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_button),
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add a photo",
                    style = MaterialTheme.typography.headlineLarge.copy(color = Color.Black),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = profileImageUri?.let { rememberAsyncImagePainter(it) }
                        ?: painterResource(R.drawable.human_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    // Inside your "Upload" Column -> Modifier.clickable { ... }
                    modifier = Modifier.clickable {
                        val selectedUri = profileImageUri
                        if (selectedUri != null) {
                            try {
                                val file = photoViewModel.uriToFile(selectedUri)
                                if (file.exists()) {
                                    authViewModel.uploadProfilePhoto(file)

                                    Toast.makeText(context, "Profile uploaded!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("home-screen") {
                                        popUpTo("addphoto-screen") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Failed to convert image to file", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error uploading photo: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
                        }
                    }

                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Upload",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Upload",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilledButton(
                    onClick = { showBottomSheet = true },
                    title = "Choose a photo"
                )
                Spacer(modifier = Modifier.height(10.dp))
                WhiteButtonWithStroke(
                    onClick = {
                        photoViewModel.clearProfileImage() // ✅ Clear temp image from last user
                        navController.navigate("home-screen") {
                            popUpTo("addphoto-screen") { inclusive = true }
                        }
                    },
                    title = "Maybe Later"
                )

            }
        }
    }
}






























