
package com.example.quba.ui.screen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
    public var loc: Int =0
    public var sub: String = ""
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
    val isFormValid = className.isNotBlank() && username.isNotBlank() && password.isNotBlank()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SoftGrey)
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AccentCream
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftBlue.copy(alpha = 0.1f))
                            .semantics { contentDescription = "Back to main screen" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }

                    Text(
                        text = "Teacher Login",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp,
                            color = Color.DarkGray
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .semantics { contentDescription = "Teacher Login Title" }
                    )

                    // Class Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = className,
                            onValueChange = {},
                            label = { Text("Class Name", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Class selection field" },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                        contentDescription = "Toggle class dropdown",
                                        tint = Color.DarkGray
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoftBlue,
                                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                                errorBorderColor = MaterialTheme.colorScheme.error
                            )
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(AccentCream, AccentCream.copy(alpha = 0.9f))
                                    )
                                )
                        ) {
                            availableClasses.forEach { classItem ->
                                DropdownMenuItem(
                                    text = { Text(classItem, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.DarkGray)) },
                                    onClick = {
                                        className = classItem
                                        expanded = false
                                        errorMessage = null
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (className == classItem) SoftBlue.copy(alpha = 0.1f) else Color.Transparent
                                        )
                                        .semantics { contentDescription = "Select $classItem" }
                                )
                            }
                        }
                    }

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = username,
                        onValueChange = {
                            username = it
                            errorMessage = null
                        },
                        label = "Username",
                        contentDescription = "Username input field",
                        isError = errorMessage != null
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = "Password",
                        contentDescription = "Password input field",
                        isError = errorMessage != null,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = if (showPassword) "Hide password" else "Show password",
                                    tint = Color.DarkGray
                                )
                            }
                        }
                    )

                    AnimatedVisibility(
                        visible = errorMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        errorMessage?.let {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier.semantics { contentDescription = "Error: $it" }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            // Replace with secure authentication in production
                            if (className.isNotBlank() && username == "teacher" && password == "password") {
                                onLoginSuccess(className)
                                loc=1
                                sub = className
                            } else {
                                errorMessage = "Invalid credentials or missing class"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SoftBlue, SoftBlue.copy(alpha = 0.8f))
                                )
                            )
                            .semantics { contentDescription = "Login button" },
                        enabled = isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            disabledContainerColor = SoftBlue.copy(alpha = 0.3f),
                            disabledContentColor = Color.White.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 6.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = "Login",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    contentDescription: String,
    isError: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)) },
        modifier = modifier
            .fillMaxWidth()
            .semantics { this.contentDescription = contentDescription },
        isError = isError,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SoftBlue,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
}
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TeacherScreenPreview() {
    TeacherScreen(
        modifier = Modifier,
        onBack = {},
        onLoginSuccess = {}
    )
}