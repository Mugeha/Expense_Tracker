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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    var password by remember { mutableStateOf("") }
//    val image = painterResource(R.drawable.waving_hand)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.HomeColor))
//            .padding(top = 100.dp)
//            .padding(),
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
            verticalAlignment = Alignment.CenterVertically // Center the row's content vertically
        ) {
            Text(
                text = "Forgot Password?",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 16.dp) // Adjust top padding to align with the image
            )
            Spacer(modifier = Modifier.width(10.dp))
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

        }
        Text(
            text = "Forgot email?",
            style = TextStyle(
                color = colorResource(id = R.color.ForgotPasswordColor),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,

                ),
            modifier = Modifier
                .align(Alignment.End)
                .clickable{ navController.navigate("enter-phone-number")}
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 40.dp)

        ){
            FilledButton(title = "Send Reset Link", destination="reset-pwd", navController = navController)
            Spacer(modifier = Modifier.height(16.dp)) // Add a small spacer for minimal space

            // Login Prompt

        }
        Text(
            text = "Back to Login",
modifier = Modifier.clickable{navController.navigate("login-screen")},
            color = colorResource(id = R.color.ForgotPasswordColor),
        )
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
    var username by remember { mutableStateOf("") }
    val otp by remember { mutableStateOf("") }
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
            text = "Reset Password",
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
            FilledButton(title = "Reset Password", destination = "login-screen", navController = navController)
        }
    }
}

@Composable
fun OTPResetPage(navController: NavController) {
    var phoneno by remember { mutableStateOf("") }
    var confirmphoneno by remember { mutableStateOf("") }
//    val image = painterResource(R.drawable.waving_hand)

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
            verticalAlignment = Alignment.CenterVertically // Center the row's content vertically
        ) {
            Text(
                text = "Enter Phone Number",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 20.dp) // Adjust top padding to align with the image
            )
            Spacer(modifier = Modifier.width(10.dp))
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
            text = "We will send an OTP verification number to your phone number",
            style = TextStyle(fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Phone Number Field
            CustomUnderlineTextField(
                value = phoneno,
                onValueChange = { phoneno = it },
                placeholder = "+254 7********"
            )
            CustomUnderlineTextField(
                value = confirmphoneno,
                onValueChange = { confirmphoneno = it },
                placeholder = "Confirm Phone Number"
            )

        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 20.dp)

        ){
            FilledButton(title = "Send me the code", destination = "enter-code", navController = navController)
            Spacer(modifier = Modifier.height(10.dp)) // Add a small spacer for minimal space

            // Login Prompt

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
                style = TextStyle(
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 16.dp) // Adjust top padding to align with the image
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
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f)
            ),
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


