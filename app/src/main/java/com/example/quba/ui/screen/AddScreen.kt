package com.example.quba.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.app.DatePickerDialog
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.example.quba.utils.StudentViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Colors from AdminScreen
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)
private val DisabledColor = Color(0xFFADB5BD)

@Composable
fun AddStudentScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: StudentViewModel = viewModel()
) {
    val classes = listOf("Nursery", "KG", "1st", "2nd", "3rd", "4th", "5th")
    val genders = listOf("Male", "Female", "Other")
    val categories = listOf("General", "OBC", "SC", "ST", "Other")
    val transportOptions = listOf("School Bus", "Private", "Other")
    var studentName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Male") }
    var nationality by remember { mutableStateOf("") }
    var aadhaarNumber by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var guardianName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var fatherOccupation by remember { mutableStateOf("") }
    var motherOccupation by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("Nursery") }
    var previousSchoolName by remember { mutableStateOf("") }
    var previousSchoolAddress by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("General") }
    var religion by remember { mutableStateOf("") }
    var motherTongue by remember { mutableStateOf("") }
    var siblingsInSchool by remember { mutableStateOf("") }
    var selectedTransport by remember { mutableStateOf("Private") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoadingSave by remember { mutableStateOf(false) }
    var isLoadingClear by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    val isFormValid = studentName.isNotBlank() && dateOfBirth.isNotBlank() && selectedGender.isNotBlank() &&
            nationality.isNotBlank() && fatherName.isNotBlank() && motherName.isNotBlank() &&
            contactNumber.isNotBlank() && emailAddress.isNotBlank() && fatherOccupation.isNotBlank() &&
            motherOccupation.isNotBlank() && address.isNotBlank() && selectedClass.isNotBlank() &&
            selectedCategory.isNotBlank() && religion.isNotBlank() && motherTongue.isNotBlank() &&
            selectedTransport.isNotBlank() &&
            contactNumber.matches(Regex("^[0-9]{10}$")) &&
            (aadhaarNumber.isEmpty() || aadhaarNumber.matches(Regex("^[0-9]{12}$"))) &&
            emailAddress.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) &&
            studentName.matches(Regex("^[A-Za-z\\s]*$")) &&
            nationality.matches(Regex("^[A-Za-z\\s]*$")) &&
            fatherName.matches(Regex("^[A-Za-z\\s]*$")) &&
            motherName.matches(Regex("^[A-Za-z\\s]*$")) &&
            (guardianName.isEmpty() || guardianName.matches(Regex("^[A-Za-z\\s]*$"))) &&
            fatherOccupation.matches(Regex("^[A-Za-z\\s]*$")) &&
            motherOccupation.matches(Regex("^[A-Za-z\\s]*$")) &&
            religion.matches(Regex("^[A-Za-z\\s]*$")) &&
            motherTongue.matches(Regex("^[A-Za-z\\s]*$"))

    // Clear confirmation dialog
    if (showClearDialog) {
        Dialog(
            onDismissRequest = { showClearDialog = false }
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Clear Form?",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Text(
                        text = "This will reset all fields.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showClearDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DisabledColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                isLoadingClear = true
                                kotlinx.coroutines.MainScope().launch {
                                    delay(1000) // Simulate clearing
                                    studentName = ""
                                    dateOfBirth = ""
                                    selectedGender = "Male"
                                    nationality = ""
                                    aadhaarNumber = ""
                                    fatherName = ""
                                    motherName = ""
                                    guardianName = ""
                                    contactNumber = ""
                                    emailAddress = ""
                                    fatherOccupation = ""
                                    motherOccupation = ""
                                    address = ""
                                    selectedClass = "Nursery"
                                    previousSchoolName = ""
                                    previousSchoolAddress = ""
                                    selectedCategory = "General"
                                    religion = ""
                                    motherTongue = ""
                                    siblingsInSchool = ""
                                    selectedTransport = "Private"
                                    errorMessage = null
                                    isLoadingClear = false
                                    showClearDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ErrorColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isLoadingClear) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Clear")
                            }
                        }
                    }
                }
            }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(40.dp)
                                    .semantics { contentDescription = "Back to previous screen" }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = TextColor
                                )
                            }

                            Text(
                                text = "Add Student",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextColor,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                                    .semantics { contentDescription = "Add Student Title" }
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
                                imageVector = Icons.Filled.PersonAdd,
                                contentDescription = "Add student icon",
                                tint = PrimaryColor,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Text(
                            text = "Register a new student",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextColor.copy(alpha = 0.6f),
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Student Details", style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextColor,
                                fontFamily = FontFamily.SansSerif
                            ), modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            textAlign = TextAlign.Start)

                        OutlinedTextField(
                            value = studentName,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    studentName = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Student Name",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Student Name input field" },
                            isError = errorMessage != null && studentName.isBlank(),
                            supportingText = errorMessage?.takeIf { studentName.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        DatePickeField(
                            modifier = Modifier.fillMaxWidth(),
                            date = dateOfBirth,
                            onDateSelected = {
                                dateOfBirth = it
                                errorMessage = null
                            },
                            isError = errorMessage != null && dateOfBirth.isBlank()
                        )

                        var genderExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedGender,
                                onValueChange = {},
                                label = {
                                    Text(
                                        "Gender",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = TextColor.copy(alpha = 0.8f),
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { genderExpanded = true }
                                    .semantics { contentDescription = "Gender selection field" },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { genderExpanded = !genderExpanded }) {
                                        Icon(
                                            imageVector = if (genderExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                            contentDescription = "Toggle gender dropdown",
                                            tint = TextColor.copy(alpha = 0.6f)
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                    cursorColor = PrimaryColor,
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                    focusedTextColor = TextColor,
                                    unfocusedTextColor = TextColor
                                )
                            )
                            DropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CardColor)
                            ) {
                                genders.forEach { gender ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                gender,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    color = TextColor,
                                                    fontFamily = FontFamily.SansSerif
                                                )
                                            )
                                        },
                                        onClick = {
                                            selectedGender = gender
                                            genderExpanded = false
                                            errorMessage = null
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (selectedGender == gender) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent
                                            )
                                            .semantics { contentDescription = "Select $gender" }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = nationality,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    nationality = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Nationality",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Nationality input field" },
                            isError = errorMessage != null && nationality.isBlank(),
                            supportingText = errorMessage?.takeIf { nationality.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = aadhaarNumber,
                            onValueChange = {
                                if (it.length <= 12 && (it.isEmpty() || it.matches(Regex("^[0-9]*$")))) {
                                    aadhaarNumber = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Aadhaar Number (Optional)",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Aadhaar Number input field" },
                            isError = errorMessage != null && aadhaarNumber.isNotEmpty() && !aadhaarNumber.matches(Regex("^[0-9]{12}$")),
                            supportingText = errorMessage?.takeIf { aadhaarNumber.isNotEmpty() && !aadhaarNumber.matches(Regex("^[0-9]{12}$")) }?.let {
                                { Text("Must be 12 digits", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        Text(
                            text = "Parent/Guardian Details",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextColor,
                                fontFamily = FontFamily.SansSerif
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            textAlign = TextAlign.Start
                        )

                        OutlinedTextField(
                            value = fatherName,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    fatherName = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Father's Name",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Father's Name input field" },
                            isError = errorMessage != null && fatherName.isBlank(),
                            supportingText = errorMessage?.takeIf { fatherName.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = motherName,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    motherName = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Mother's Name",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Mother's Name input field" },
                            isError = errorMessage != null && motherName.isBlank(),
                            supportingText = errorMessage?.takeIf { motherName.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = guardianName,
                            onValueChange = {
                                if (it.length <= 50) {
                                    guardianName = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Guardian's Name (Optional)",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Guardian's Name input field" },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = contactNumber,
                            onValueChange = {
                                if (it.length <= 10 && it.matches(Regex("^[0-9]*$"))) {
                                    contactNumber = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Contact Number",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Contact Number input field" },
                            isError = errorMessage != null && !contactNumber.matches(Regex("^[0-9]{10}$")),
                            supportingText = errorMessage?.takeIf { !contactNumber.matches(Regex("^[0-9]{10}$")) }?.let {
                                { Text("Must be 10 digits", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = emailAddress,
                            onValueChange = {
                                if (it.length <= 100) {
                                    emailAddress = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Email Address",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Email Address input field" },
                            isError = errorMessage != null && !emailAddress.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")),
                            supportingText = errorMessage?.takeIf { !emailAddress.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) }?.let {
                                { Text("Invalid email", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = fatherOccupation,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    fatherOccupation = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Father's Occupation",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Father's Occupation input field" },
                            isError = errorMessage != null && fatherOccupation.isBlank(),
                            supportingText = errorMessage?.takeIf { fatherOccupation.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = motherOccupation,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    motherOccupation = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    text = "Mother's Occupation",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Mother's Occupation input field" },
                            isError = errorMessage != null && motherOccupation.isBlank(),
                            supportingText = errorMessage?.takeIf { motherOccupation.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = address,
                            onValueChange = {
                                if (it.length <= 200) {
                                    address = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Address",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Address input field" },
                            isError = errorMessage != null && address.isBlank(),
                            supportingText = errorMessage?.takeIf { address.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = false,
                            maxLines = 3,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        Text(
                            text = "Academic Details",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextColor,
                                fontFamily = FontFamily.SansSerif
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            textAlign = TextAlign.Start
                        )

                        var classExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedClass,
                                onValueChange = {},
                                label = {
                                    Text(
                                        "Class",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = TextColor.copy(alpha = 0.8f),
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { classExpanded = true }
                                    .semantics { contentDescription = "Class selection field" },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { classExpanded = !classExpanded }) {
                                        Icon(
                                            imageVector = if (classExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                            contentDescription = "Toggle class dropdown",
                                            tint = TextColor.copy(alpha = 0.6f)
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                    cursorColor = PrimaryColor,
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                    focusedTextColor = TextColor,
                                    unfocusedTextColor = TextColor
                                )
                            )
                            DropdownMenu(
                                expanded = classExpanded,
                                onDismissRequest = { classExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CardColor)
                            ) {
                                classes.forEach { className ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                className,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    color = TextColor,
                                                    fontFamily = FontFamily.SansSerif
                                                )
                                            )
                                        },
                                        onClick = {
                                            selectedClass = className
                                            classExpanded = false
                                            errorMessage = null
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (selectedClass == className) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent
                                            )
                                            .semantics { contentDescription = "Select $className" }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = previousSchoolName,
                            onValueChange = {
                                if (it.length <= 100) {
                                    previousSchoolName = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Previous School Name (Optional)",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Previous School Name input field" },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = previousSchoolAddress,
                            onValueChange = {
                                if (it.length <= 200) {
                                    previousSchoolAddress = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Previous School Address (Optional)",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Previous School Address input field" },
                            singleLine = false,
                            maxLines = 3,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        Text(
                            text = "Additional Details",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextColor,
                                fontFamily = FontFamily.SansSerif
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            textAlign = TextAlign.Start
                        )

                        var categoryExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                label = {
                                    Text(
                                        "Category/Caste",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = TextColor.copy(alpha = 0.8f),
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { categoryExpanded = true }
                                    .semantics { contentDescription = "Category/Caste selection field" },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { categoryExpanded = !categoryExpanded }) {
                                        Icon(
                                            imageVector = if (categoryExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                            contentDescription = "Toggle category dropdown",
                                            tint = TextColor.copy(alpha = 0.6f)
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                    cursorColor = PrimaryColor,
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                    focusedTextColor = TextColor,
                                    unfocusedTextColor = TextColor
                                )
                            )
                            DropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CardColor)
                            ) {
                                categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                category,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    color = TextColor,
                                                    fontFamily = FontFamily.SansSerif
                                                )
                                            )
                                        },
                                        onClick = {
                                            selectedCategory = category
                                            categoryExpanded = false
                                            errorMessage = null
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (selectedCategory == category) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent
                                            )
                                            .semantics { contentDescription = "Select $category" }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = religion,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    religion = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Religion",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Religion input field" },
                            isError = errorMessage != null && religion.isBlank(),
                            supportingText = errorMessage?.takeIf { religion.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = motherTongue,
                            onValueChange = {
                                if (it.length <= 50 && it.matches(Regex("^[A-Za-z\\s]*$"))) {
                                    motherTongue = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Mother Tongue",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Mother Tongue input field" },
                            isError = errorMessage != null && motherTongue.isBlank(),
                            supportingText = errorMessage?.takeIf { motherTongue.isBlank() }?.let {
                                { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        OutlinedTextField(
                            value = siblingsInSchool,
                            onValueChange = {
                                if (it.length <= 100) {
                                    siblingsInSchool = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Siblings in School (Optional)",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Siblings in School input field" },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        var transportExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedTransport,
                                onValueChange = {},
                                label = {
                                    Text(
                                        "Transportation",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = TextColor.copy(alpha = 0.8f),
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { transportExpanded = true }
                                    .semantics { contentDescription = "Transportation selection field" },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { transportExpanded = !transportExpanded }) {
                                        Icon(
                                            imageVector = if (transportExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                            contentDescription = "Toggle transportation dropdown",
                                            tint = TextColor.copy(alpha = 0.6f)
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                    cursorColor = PrimaryColor,
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                    focusedTextColor = TextColor,
                                    unfocusedTextColor = TextColor
                                )
                            )
                            DropdownMenu(
                                expanded = transportExpanded,
                                onDismissRequest = { transportExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CardColor)
                            ) {
                                transportOptions.forEach { transport ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                transport,
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                    color = TextColor,
                                                    fontFamily = FontFamily.SansSerif
                                                )
                                            )
                                        },
                                        onClick = {
                                            selectedTransport = transport
                                            transportExpanded = false
                                            errorMessage = null
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (selectedTransport == transport) PrimaryColor.copy(alpha = 0.1f) else Color.Transparent
                                            )
                                            .semantics { contentDescription = "Select $transport" }
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (isFormValid) {
                                        try {
                                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
                                                    isLoadingSave = true
                                                    kotlinx.coroutines.MainScope().launch {
                                                        delay(1000) // Simulate backend save
                                                        viewModel.addStudent(
                                                            Student(
                                                                id = 0, // Assuming ID is auto-generated
                                                                name = studentName,
                                                                className = selectedClass
                                                            )
                                                        )
                                                        onSaveSuccess()
                                                        isLoadingSave = false
                                                    }
                                                }
                                            }
                                        } catch (e: Exception) {
                                            errorMessage = "Invalid date format (use dd/MM/yyyy)"
                                            isLoadingSave = false
                                        }
                                    } else {
                                        errorMessage = when {
                                            !contactNumber.matches(Regex("^[0-9]{10}$")) -> "Contact number must be 10 digits"
                                            aadhaarNumber.isNotEmpty() && !aadhaarNumber.matches(Regex("^[0-9]{12}$")) -> "Aadhaar number must be 12 digits"
                                            !emailAddress.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) -> "Invalid email address"
                                            studentName.isBlank() -> "Student name is required"
                                            dateOfBirth.isBlank() -> "Date of birth is required"
                                            nationality.isBlank() -> "Nationality is required"
                                            fatherName.isBlank() -> "Father's name is required"
                                            motherName.isBlank() -> "Mother's name is required"
                                            contactNumber.isBlank() -> "Contact number is required"
                                            emailAddress.isBlank() -> "Email address is required"
                                            fatherOccupation.isBlank() -> "Father's occupation is required"
                                            motherOccupation.isBlank() -> "Mother's occupation is required"
                                            address.isBlank() -> "Address is required"
                                            selectedClass.isBlank() -> "Class is required"
                                            selectedCategory.isBlank() -> "Category is required"
                                            religion.isBlank() -> "Religion is required"
                                            motherTongue.isBlank() -> "Mother tongue is required"
                                            selectedTransport.isBlank() -> "Transportation is required"
                                            else -> "Please fill all required fields"
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .semantics { contentDescription = "Save button" },
                                enabled = isFormValid && !isLoadingSave,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryColor,
                                    contentColor = Color.White,
                                    disabledContainerColor = DisabledColor,
                                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation =4.dp,
                                    pressedElevation = 8.dp,
                                    disabledElevation = 0.dp
                                )
                            ) {
                                if (isLoadingSave) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White
                                    )
                                } else {
                                    Text(
                                        text = "SAVE",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = { showClearDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .semantics { contentDescription = "Clear Form button" },
                                enabled = !isLoadingClear,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ErrorColor,
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
                                if (isLoadingClear) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White
                                    )
                                } else {
                                    Text(
                                        text = "CLEAR",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Need help?",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = PrimaryColor,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif
                            ),
                            modifier = Modifier
                                .clickable { /* Handle help request */ }
                                .padding(top = 16.dp)
                                .semantics { contentDescription = "Need help link" }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DatePickeField(
    modifier: Modifier = Modifier,
    date: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    OutlinedTextField(
        value = date,
        onValueChange = {},
        label = {
            Text(
                "Date of Birth",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextColor.copy(alpha = 0.8f),
                    fontFamily = FontFamily.SansSerif
                )
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }.time
                        onDateSelected(dateFormat.format(selectedDate))
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
                datePickerDialog.show()
            }
            .semantics { contentDescription = "Date of Birth input field" },
        readOnly = true,
        isError = isError,
        supportingText = isError.let { { Text("Required", color = ErrorColor, fontFamily = FontFamily.SansSerif) } },
        trailingIcon = {
            IconButton(onClick = {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth)
                        }.time
                        onDateSelected(dateFormat.format(selectedDate))
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
                datePickerDialog.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Open date picker",
                    tint = TextColor.copy(alpha = 0.6f)
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
            cursorColor = PrimaryColor,
            focusedLabelColor = PrimaryColor,
            unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor
        )
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, widthDp = 720, heightDp = 1280, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddStudentScreenPreview() {
    MaterialTheme {
        AddStudentScreen(
            onBack = {},
            onSaveSuccess = {}
        )
    }
}