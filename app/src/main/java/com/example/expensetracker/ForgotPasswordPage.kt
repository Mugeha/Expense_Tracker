package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun ForgotPasswordPage(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var emailTouched by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val isValidEmail = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isEmailInvalid = emailTouched && !isValidEmail

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
            .padding(horizontal = 30.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Forgot Password?",
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            modifier = Modifier.padding(top = 80.dp, bottom = 16.dp)
        )

        Text(
            text = "Please fill in your details to proceed",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .widthIn(max = minOf(500.dp, screenWidth * 0.8f)) // Responsive
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Email Field
            CustomUnderlineTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true // Mark as touched
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

            // Forgot Email? Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Forgot email?",
                    style = MaterialTheme.typography.labelLarge.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
                    modifier = Modifier.clickable {
                        navController.navigate("enter-phone-number")
                    }
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            // ✅ Disable button when invalid
            FilledButton(
                title = "Send Reset Link",
                enabled = isValidEmail,
                onClick = { navController.navigate("reset-pwd") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.labelSmall.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
                modifier = Modifier.clickable { navController.navigate("login-screen") }
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ForgotPasswordPagePreview() {
    ExpenseTrackerTheme {
        ForgotPasswordPage(navController = rememberNavController())
    }
}


@Preview (showBackground = true)
@Composable
fun ResetPasswordPreview() {
    ExpenseTrackerTheme {
        ResetPassword(navController = rememberNavController())
    }
}

@Composable
fun ResetPassword(navController: NavController) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordTouched by remember { mutableStateOf(false) }
    var confirmPasswordTouched by remember { mutableStateOf(false) }

    val isPasswordValid = password.length >= 6
    val isPasswordMatch = confirmPassword == password
    val showPasswordError = passwordTouched && !isPasswordValid
    val showConfirmPasswordError = confirmPasswordTouched && !isPasswordMatch

    val isFormValid = isPasswordValid && isPasswordMatch

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
            .padding(horizontal = 30.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            modifier = Modifier.padding(top = 80.dp, bottom = 16.dp)
        )

        Text(
            text = "Please fill in your details to proceed",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .widthIn(max = minOf(500.dp, screenWidth * 0.8f)) // Responsive layout
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Password Field
            CustomUnderlineTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordTouched = true
                },
                placeholder = "Password",
                isPassword = true,
                isError = showPasswordError
            )

            if (showPasswordError) {
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
                isError = showConfirmPasswordError
            )

            if (showConfirmPasswordError) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            FilledButton(
                title = "Reset Password",
                enabled = isFormValid, // ✅ Button is disabled if invalid
                onClick = {
                    navController.navigate("login-screen")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.labelSmall.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
                modifier = Modifier.clickable { navController.navigate("login-screen") }
            )
        }
    }
}



@Composable
fun OTPResetPage(navController: NavController) {
    var phoneno by remember { mutableStateOf("") }
    var confirmphoneno by remember { mutableStateOf("") }
    var phoneTouched by remember { mutableStateOf(false) }
    var confirmPhoneTouched by remember { mutableStateOf(false) }

    val isPhoneValid = phoneno.matches(Regex("^\\+2547\\d{8}$"))
    val isPhoneMatch = phoneno == confirmphoneno
    val showPhoneError = phoneTouched && !isPhoneValid
    val showConfirmPhoneError = confirmPhoneTouched && !isPhoneMatch

    val isFormValid = isPhoneValid && isPhoneMatch

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
            .padding(horizontal = 30.dp, vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Enter Phone Number",
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
            modifier = Modifier.padding(top = 80.dp, bottom = 16.dp)
        )

        Text(
            text = "We will send an OTP verification number to your phone",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f))
        )

        Column(
            modifier = Modifier
                .widthIn(max = minOf(500.dp, screenWidth * 0.8f))
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Phone Number Field
            CustomUnderlineTextField(
                value = phoneno,
                onValueChange = {
                    phoneno = it
                    phoneTouched = true
                },
                placeholder = "+254 7********",
                isError = showPhoneError
            )

            if (showPhoneError) {
                Text(
                    text = "Enter a valid Kenyan phone number",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Confirm Phone Number Field
            CustomUnderlineTextField(
                value = confirmphoneno,
                onValueChange = {
                    confirmphoneno = it
                    confirmPhoneTouched = true
                },
                placeholder = "Confirm Phone Number",
                isError = showConfirmPhoneError
            )

            if (showConfirmPhoneError) {
                Text(
                    text = "Phone numbers do not match",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp)
        ) {
            FilledButton(
                title = "Send me the code",
                onClick = {},
                enabled = isFormValid // ✅ Button only enabled when valid
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Back to Login",
                style = MaterialTheme.typography.labelSmall.copy(color = colorResource(id = R.color.ForgotPasswordColor)),
                modifier = Modifier.clickable { navController.navigate("login-screen") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmNumberPreview() {
    ExpenseTrackerTheme {
        ConfirmNumber(navController = rememberNavController())
    }
}

@Composable
fun ConfirmNumber(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
//            .padding(top = 100.dp)
            .padding(
                start = 15.dp, // Left padding
                end = 15.dp,   // Right padding
                top = 100.dp,  // Top padding
                bottom = 50.dp // Bottom padding
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically // Center the row's content vertically
        ) {
            Text(
                text = "Enter Code",
                style = MaterialTheme.typography.headlineLarge.copy(color = Color.White),
                modifier = Modifier.padding(top = 80.dp, bottom = 16.dp)
            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Image(
//                painter = image,
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(34.dp)
//            )
        }
//

        Text(
            text = "Enter the code we sent to the number ending in +254 ******7334",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.8f)),

                    modifier = Modifier.padding(start = 16.dp, end = 16.dp) // Add left padding
        )


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 10.dp)

        ){
            VerificationCodeInput()

            Spacer(modifier = Modifier.height(10.dp)) // Add a small spacer for minimal space
            WhiteButtonConfirmNumber(title = "Confirm", destination = "reset-pwd", navController = navController)
            // Login Prompt
            Text(
                text = "Send code again",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.ForgotPasswordColor),
                ),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp) // Add left padding
            )
        }

    }
}
@Preview(showBackground = true)
@Composable
fun OTPResetPagePreview() {
    ExpenseTrackerTheme {
        OTPResetPage(navController = rememberNavController())
    }
}

@Composable
fun WhiteButtonConfirmNumber(onClick: () -> Unit = {}, title: String, navController: NavController, destination: String) {
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

@Preview(showBackground = true)
@Composable
fun WhiteButtonConfirmNumberPreview() {
    ExpenseTrackerTheme {
        val navController = rememberNavController() // Create a dummy NavController for preview
        WhiteButtonConfirmNumber(title = "Confirm", navController = navController, destination = "reset-pwd")
    }
}


