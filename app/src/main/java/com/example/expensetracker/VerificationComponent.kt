package com.example.expensetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun VerificationCodeInput() {
    var code by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        for (i in 0 until 4) {
            val digit = if (i < code.length) code[i].toString() else ""
            VerificationDigitBox(
                digit = digit,
                onDigitChange = { newDigit ->
                    if (newDigit.length <= 1) {
                        code = code.take(i) + newDigit + code.drop(i + 1)
                    }
                },
                modifier = Modifier.size(68.dp) // Set a smaller size for all boxes
            )
        }
    }
}

@Composable
fun VerificationDigitBox(
    digit: String,
    onDigitChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = digit,
        onValueChange = onDigitChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = Color.White
        ),
        modifier = modifier
            .size(48.dp) // Set size of the box
            .aspectRatio(1f) // Ensure the box remains square
            .background(
                color = colorResource(id = R.color.verificationColor),
                shape = MaterialTheme.shapes.medium
            )
            .border(
                2.dp,
                color = colorResource(id = R.color.verificationColor),
                shape = MaterialTheme.shapes.medium
            )
            .padding(20.dp) // Optional: Add padding inside the box
    )
}
@Preview(showBackground = true)
@Composable
fun VerificationCodeInputPreview() {
    ExpenseTrackerTheme {
        VerificationCodeInput()
    }
}