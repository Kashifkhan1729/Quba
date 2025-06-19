package com.example.quba.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.quba.*

// Colors from AdminScreen
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)

private val DisabledColor = Color(0xFFADB5BD)

@Composable
fun TeacherScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    availableClasses: List<String> = listOf("Nursery", "KG", "Ist", "2nd", "3rd", "4th", "5th")
) {
    var className by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val isFormValid = className.isNotBlank() && username.matches(Regex("^[A-Za-z0-9_]{3,}$")) && password.length >= 6

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
                            imageVector = Icons.Filled.School,
                            contentDescription = "Teacher icon",
                            tint = PrimaryColor,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "Teacher Portal",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = FontFamily.SansSerif
                        ),
                        modifier = Modifier.semantics { contentDescription = "Teacher Portal Title" }
                    )

                    Text(
                        text = "Sign in to access your teacher dashboard",
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
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = className,
                            onValueChange = {},
                            label = {
                                Text(
                                    "Class Name",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Class selection field" },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = "Toggle class dropdown",
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
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let { { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) } }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardColor)
                        ) {
                            availableClasses.forEach { classItem ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            classItem,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = TextColor,
                                                fontFamily = FontFamily.SansSerif
                                            )
                                        )
                                    },
                                    onClick = {
                                        className = classItem
                                        expanded = false
                                        errorMessage = null
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (className == classItem) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent
                                        )
                                        .semantics { contentDescription = "Select $classItem" }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = username,
                        onValueChange = {
                            if (it.length <= 20) username = it
                            errorMessage = null
                        },
                        label = {
                            Text(
                                "Username",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextColor.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Username input field" },
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
                        value = password,
                        onValueChange = {
                            if (it.length <= 20) password = it
                            errorMessage = null
                        },
                        label = {
                            Text(
                                "Password",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextColor.copy(alpha = 0.8f),
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Password input field" },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPassword) "Hide password" else "Show password",
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
                            delay(1000) // Simulate authentication
                            if (isFormValid && username == "teacher" && password == "password") {
                                onLoginSuccess(className)
                                loc=1
                                sub=className
                            } else {
                                errorMessage = "Invalid credentials or missing class"
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
                    text = "Forgot credentials?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = PrimaryColor,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = Modifier
                        .clickable { /* Handle forgot credentials */ }
                        .semantics { contentDescription = "Forgot credentials link" }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, widthDp = 720, heightDp = 1280, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TeacherScreenPreview() {
    MaterialTheme {
        TeacherScreen(
            modifier = Modifier,
            onBack = {},
            onLoginSuccess = {}
        )
    }
}