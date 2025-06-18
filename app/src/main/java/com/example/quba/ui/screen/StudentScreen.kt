
package com.example.quba.ui.screen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.app.DatePickerDialog
import androidx.compose.material.icons.filled.Error

// Color palette for consistency
val SoftBlue = Color(0xFF90CAF9) // Light blue for buttons
val SoftGrey = Color(0xFFF5F5F5) // Light grey for background
val AccentCream = Color(0xFFFFF3E0) // Creamy accent for card

@Composable
fun StudentScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onBack: () -> Unit
) {
    var rollNo by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf(DateUtils.getCurrentDate()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isFormValid = rollNo.isNotBlank() && dateOfBirth.isNotBlank()
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
                        text = "Student Login",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp,
                            color = Color.DarkGray
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .semantics { contentDescription = "Student Login Title" }
                    )

                    CustomTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = rollNo,
                        onValueChange = {
                            rollNo = it
                            errorMessage = null
                        },
                        label = "Roll Number",
                        contentDescription = "Roll Number input field",
                        isError = errorMessage != null

                    )

                    CustomTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() },
                        value = dateOfBirth,
                        onValueChange = { /* Ignored, as field is read-only */ },
                        label = "Date of Birth",
                        contentDescription = "Date of Birth input field",
                        isError = errorMessage != null,
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { datePickerDialog.show() }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Open date picker",
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
                                errorMessage = "Please fill all fields"
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

object DateUtils {
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return sdf.format(Date())
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
    readOnly: Boolean = false,
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
        singleLine = true,
        readOnly = readOnly,
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
fun StudentScreenPreview() {
    StudentScreen(
        modifier = Modifier,
        onLogin = {},
        onBack = {}
    )
}