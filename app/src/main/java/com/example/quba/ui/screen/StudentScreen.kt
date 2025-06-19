package com.example.quba.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Colors from AdminScreen
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)

private val DisabledColor = Color(0xFFADB5BD)

@Composable
fun StudentScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onBack: () -> Unit
) {
    var rollNo by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf(DateUtils.getCurrentDate()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val isFormValid = rollNo.matches(Regex("^[A-Za-z0-9]{3,}$")) && dateOfBirth.isNotBlank()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

    // Set up date picker with restrictions
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val selectedDate = calendar.time
                val currentDate = Date()
                val minDate = Calendar.getInstance().apply {
                    add(Calendar.YEAR, -100) // Max age 100 years
                }.time

                if (selectedDate.after(currentDate)) {
                    errorMessage = "Date of birth cannot be in the future"
                } else if (selectedDate.before(minDate)) {
                    errorMessage = "Date of birth is too far in the past"
                } else {
                    dateOfBirth = dateFormat.format(selectedDate)
                    errorMessage = null
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
            datePicker.minDate = Calendar.getInstance().apply {
                add(Calendar.YEAR, -100)
            }.timeInMillis
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundColor, Color.White)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PrimaryColor, SecondaryColor)
                    )
                )
                .align(Alignment.TopCenter)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .align(Alignment.Center)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = PrimaryColor.copy(alpha = 0.2f)
                ),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .size(40.dp)
                            .semantics { contentDescription = "Back to role selection" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = TextColor
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimaryColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Student icon",
                            tint = PrimaryColor,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "Student Portal",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = FontFamily.SansSerif
                        ),
                        modifier = Modifier.semantics { contentDescription = "Student Portal Title" }
                    )

                    Text(
                        text = "Sign in to access your student dashboard",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = rollNo,
                        onValueChange = {
                            if (it.length <= 20) rollNo = it
                            errorMessage = null
                        },
                        label = {
                            Text(
                                "Roll Number",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextColor.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Roll Number input field" },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                            cursorColor = PrimaryColor,
                            focusedLabelColor = PrimaryColor,
                            unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                            focusedTextColor = TextColor,
                            unfocusedTextColor = TextColor
                        ),
                        singleLine = true,
                        isError = errorMessage != null,
                        supportingText = errorMessage?.let { { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) } }
                    )

                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = { /* Read-only */ },
                        label = {
                            Text(
                                "Date of Birth",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextColor.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() }
                            .semantics { contentDescription = "Date of Birth input field" },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Open date picker",
                                    tint = TextColor.copy(alpha = 0.6f)
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedBorderColor = PrimaryColor,
                            unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                            cursorColor = PrimaryColor,
                            focusedLabelColor = PrimaryColor,
                            unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                            focusedTextColor = TextColor,
                            unfocusedTextColor = TextColor
                        ),
                        singleLine = true,
                        isError = errorMessage != null,
                        supportingText = errorMessage?.let { { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) } }
                    )
                }

                Button(
                    onClick = {
                        isLoading = true
                        kotlinx.coroutines.MainScope().launch {
                            delay(1000) // Simulate validation
                            if (isFormValid) {
                                try {
                                    val parsedDate = dateFormat.parse(dateOfBirth)
                                    val currentDate = Date()
                                    val minDate = Calendar.getInstance().apply {
                                        add(Calendar.YEAR, -100)
                                    }.time

                                    if (parsedDate != null) {
                                        if (parsedDate.after(currentDate)) {
                                            errorMessage = "Date of birth cannot be in the future"
                                        } else if (parsedDate.before(minDate)) {
                                            errorMessage = "Date of birth is too far in the past"
                                        } else {
                                            onLogin()
                                        }
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Invalid date format (use MM/dd/yyyy)"
                                }
                            } else {
                                errorMessage = "Please fill all fields correctly"
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .semantics { contentDescription = "Login button" },
                    enabled = isFormValid && !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = Color.White,
                        disabledContainerColor = DisabledColor,
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "LOGIN",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }
                }

                Text(
                    text = "Forgot roll number?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PrimaryColor,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = Modifier
                        .clickable { /* Handle forgot roll number */ }
                        .semantics { contentDescription = "Forgot roll number link" }
                )
            }
        }
    }
}

object DateUtils {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, widthDp = 720, heightDp = 1280, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentScreenPreview() {
    MaterialTheme {
        StudentScreen(
            modifier = Modifier,
            onLogin = {},
            onBack = {}
        )
    }
}