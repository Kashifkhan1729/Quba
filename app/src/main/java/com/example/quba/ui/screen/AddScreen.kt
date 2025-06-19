package com.example.quba.ui.screen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.app.DatePickerDialog
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.example.quba.ui.theme.*


@Composable
fun AddStudentScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
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
    val isFormValid = studentName.isNotBlank() && dateOfBirth.isNotBlank() && selectedGender.isNotBlank() &&
            nationality.isNotBlank() && fatherName.isNotBlank() && motherName.isNotBlank() &&
            contactNumber.isNotBlank() && emailAddress.isNotBlank() && fatherOccupation.isNotBlank() &&
            motherOccupation.isNotBlank() && address.isNotBlank() && selectedClass.isNotBlank() &&
            selectedCategory.isNotBlank() && religion.isNotBlank() && motherTongue.isNotBlank() &&
            selectedTransport.isNotBlank() &&
            contactNumber.matches(Regex("^[0-9]{10}$")) &&
            (aadhaarNumber.isEmpty() || aadhaarNumber.matches(Regex("^[0-9]{12}$"))) &&
            emailAddress.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = AccentCream),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SoftBlue.copy(alpha = 0.1f))
                                    .semantics { contentDescription = "Back to previous screen" }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = Color.DarkGray
                                )
                            }

                            Text(
                                text = "Add Student",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 24.sp,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .semantics { contentDescription = "Add Student Title" },
                                textAlign = TextAlign.Center
                            )

                            // Student Personal Information
                            Text(
                                text = "Student Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 4.dp),
                                textAlign = TextAlign.Start
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = studentName,
                                onValueChange = {
                                    studentName = it
                                    errorMessage = null
                                },
                                label = "Student Name",
                                contentDescription = "Student Name input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            DatePickeField(
                                modifier = Modifier.fillMaxWidth(),
                                date = dateOfBirth,
                                onDateSelected = {
                                    dateOfBirth = it
                                    errorMessage = null
                                },
                                isError = errorMessage != null
                            )

                            var genderExpanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = selectedGender,
                                    onValueChange = {},
                                    label = "Gender",
                                    contentDescription = "Gender selection field",
                                    isError = errorMessage != null,
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { genderExpanded = !genderExpanded }) {
                                            Icon(
                                                imageVector = if (genderExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                contentDescription = "Toggle gender dropdown",
                                                tint = Color.DarkGray
                                            )
                                        }
                                    }
                                )
                                DropdownMenu(
                                    expanded = genderExpanded,
                                    onDismissRequest = { genderExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(AccentCream, AccentCream.copy(alpha = 0.9f))
                                            )
                                        )
                                ) {
                                    genders.forEach { gender ->
                                        DropdownMenuItem(
                                            text = { Text(gender, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.DarkGray)) },
                                            onClick = {
                                                selectedGender = gender
                                                genderExpanded = false
                                                errorMessage = null
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (selectedGender == gender) SoftBlue.copy(alpha = 0.1f) else Color.Transparent
                                                )
                                                .semantics { contentDescription = "Select $gender" }
                                        )
                                    }
                                }
                            }

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = nationality,
                                onValueChange = {
                                    nationality = it
                                    errorMessage = null
                                },
                                label = "Nationality",
                                contentDescription = "Nationality input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = aadhaarNumber,
                                onValueChange = {
                                    aadhaarNumber = it
                                    errorMessage = null
                                },
                                label = "Aadhaar Number (Optional)",
                                contentDescription = "Aadhaar Number input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            // Parent/Guardian Information
                            Text(
                                text = "Parent/Guardian Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 4.dp),
                                textAlign = TextAlign.Start
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = fatherName,
                                onValueChange = {
                                    fatherName = it
                                    errorMessage = null
                                },
                                label = "Father's Name",
                                contentDescription = "Father's Name input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = motherName,
                                onValueChange = {
                                    motherName = it
                                    errorMessage = null
                                },
                                label = "Mother's Name",
                                contentDescription = "Mother's Name input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = guardianName,
                                onValueChange = {
                                    guardianName = it
                                    errorMessage = null
                                },
                                label = "Guardian's Name (Optional)",
                                contentDescription = "Guardian's Name input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = contactNumber,
                                onValueChange = {
                                    contactNumber = it
                                    errorMessage = null
                                },
                                label = "Contact Number",
                                contentDescription = "Contact Number input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = emailAddress,
                                onValueChange = {
                                    emailAddress = it
                                    errorMessage = null
                                },
                                label = "Email Address",
                                contentDescription = "Email Address input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = fatherOccupation,
                                onValueChange = {
                                    fatherOccupation = it
                                    errorMessage = null
                                },
                                label = "Father's Occupation",
                                contentDescription = "Father's Occupation input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = motherOccupation,
                                onValueChange = {
                                    motherOccupation = it
                                    errorMessage = null
                                },
                                label = "Mother's Occupation",
                                contentDescription = "Mother's Occupation input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = address,
                                onValueChange = {
                                    address = it
                                    errorMessage = null
                                },
                                label = "Address",
                                contentDescription = "Address input field",
                                isError = errorMessage != null,
                                singleLine = false,
                                maxLines = 3
                            )

                            // Academic Information
                            Text(
                                text = "Academic Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 4.dp),
                                textAlign = TextAlign.Start
                            )

                            var classExpanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = selectedClass,
                                    onValueChange = {},
                                    label = "Class",
                                    contentDescription = "Class selection field",
                                    isError = errorMessage != null,
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { classExpanded = !classExpanded }) {
                                            Icon(
                                                imageVector = if (classExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                contentDescription = "Toggle class dropdown",
                                                tint = Color.DarkGray
                                            )
                                        }
                                    }
                                )
                                DropdownMenu(
                                    expanded = classExpanded,
                                    onDismissRequest = { classExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(AccentCream, AccentCream.copy(alpha = 0.9f))
                                            )
                                        )
                                ) {
                                    classes.forEach { className ->
                                        DropdownMenuItem(
                                            text = { Text(className, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.DarkGray)) },
                                            onClick = {
                                                selectedClass = className
                                                classExpanded = false
                                                errorMessage = null
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (selectedClass == className) SoftBlue.copy(alpha = 0.1f) else Color.Transparent
                                                )
                                                .semantics { contentDescription = "Select $className" }
                                        )
                                    }
                                }
                            }

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = previousSchoolName,
                                onValueChange = {
                                    previousSchoolName = it
                                    errorMessage = null
                                },
                                label = "Previous School Name (Optional)",
                                contentDescription = "Previous School Name input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = previousSchoolAddress,
                                onValueChange = {
                                    previousSchoolAddress = it
                                    errorMessage = null
                                },
                                label = "Previous School Address (Optional)",
                                contentDescription = "Previous School Address input field",
                                isError = errorMessage != null,
                                singleLine = false,
                                maxLines = 3
                            )

                            // Additional Information
                            Text(
                                text = "Additional Details",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 4.dp),
                                textAlign = TextAlign.Start
                            )

                            var categoryExpanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = selectedCategory,
                                    onValueChange = {},
                                    label = "Category/Caste",
                                    contentDescription = "Category/Caste selection field",
                                    isError = errorMessage != null,
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { categoryExpanded = !categoryExpanded }) {
                                            Icon(
                                                imageVector = if (categoryExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                contentDescription = "Toggle category dropdown",
                                                tint = Color.DarkGray
                                            )
                                        }
                                    }
                                )
                                DropdownMenu(
                                    expanded = categoryExpanded,
                                    onDismissRequest = { categoryExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(AccentCream, AccentCream.copy(alpha = 0.9f))
                                            )
                                        )
                                ) {
                                    categories.forEach { category ->
                                        DropdownMenuItem(
                                            text = { Text(category, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.DarkGray)) },
                                            onClick = {
                                                selectedCategory = category
                                                categoryExpanded = false
                                                errorMessage = null
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (selectedCategory == category) SoftBlue.copy(alpha = 0.1f) else Color.Transparent
                                                )
                                                .semantics { contentDescription = "Select $category" }
                                        )
                                    }
                                }
                            }

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = religion,
                                onValueChange = {
                                    religion = it
                                    errorMessage = null
                                },
                                label = "Religion",
                                contentDescription = "Religion input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = motherTongue,
                                onValueChange = {
                                    motherTongue = it
                                    errorMessage = null
                                },
                                label = "Mother Tongue",
                                contentDescription = "Mother Tongue input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = siblingsInSchool,
                                onValueChange = {
                                    siblingsInSchool = it
                                    errorMessage = null
                                },
                                label = "Siblings in School (Optional)",
                                contentDescription = "Siblings in School input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )

                            var transportExpanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CustomTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = selectedTransport,
                                    onValueChange = {},
                                    label = "Transportation",
                                    contentDescription = "Transportation selection field",
                                    isError = errorMessage != null,
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = { transportExpanded = !transportExpanded }) {
                                            Icon(
                                                imageVector = if (transportExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                contentDescription = "Toggle transportation dropdown",
                                                tint = Color.DarkGray
                                            )
                                        }
                                    }
                                )
                                DropdownMenu(
                                    expanded = transportExpanded,
                                    onDismissRequest = { transportExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(AccentCream, AccentCream.copy(alpha = 0.9f))
                                            )
                                        )
                                ) {
                                    transportOptions.forEach { transport ->
                                        DropdownMenuItem(
                                            text = { Text(transport, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.DarkGray)) },
                                            onClick = {
                                                selectedTransport = transport
                                                transportExpanded = false
                                                errorMessage = null
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (selectedTransport == transport) SoftBlue.copy(alpha = 0.1f) else Color.Transparent
                                                )
                                                .semantics { contentDescription = "Select $transport" }
                                        )
                                    }
                                }
                            }

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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
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
                                                        // TODO: Save student data to backend
                                                        onSaveSuccess()
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                errorMessage = "Invalid date format (use dd/MM/yyyy)"
                                            }
                                        } else {
                                            errorMessage = when {
                                                !contactNumber.matches(Regex("^[0-9]{10}$")) -> "Contact number must be 10 digits"
                                                aadhaarNumber.isNotEmpty() && !aadhaarNumber.matches(Regex("^[0-9]{12}$")) -> "Aadhaar number must be 12 digits"
                                                !emailAddress.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) -> "Invalid email address"
                                                else -> "Please fill all required fields"
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(SoftBlue, SoftBlue.copy(alpha = 0.8f))
                                            )
                                        )
                                        .semantics { contentDescription = "Save button" },
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
                                        text = "Save",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }

                                Button(
                                    onClick = {
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
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(Color(0xFFE57373), Color(0xFFE57373).copy(alpha = 0.8f))
                                            )
                                        )
                                        .semantics { contentDescription = "Clear Form button" },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color(0xFFE57373).copy(alpha = 0.3f),
                                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 2.dp,
                                        pressedElevation = 6.dp,
                                        disabledElevation = 0.dp
                                    )
                                ) {
                                    Text(
                                        text = "Clear",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }
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
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)) },
        modifier = modifier
            .fillMaxWidth()
            .semantics { this.contentDescription = contentDescription },
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SoftBlue,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
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

    CustomTextField(
        modifier = modifier.clickable {
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
        },
        value = date,
        onValueChange = {},
        label = "Date of Birth",
        contentDescription = "Date of Birth input field",
        isError = isError,
        readOnly = true,
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
                    tint = Color.DarkGray
                )
            }
        }
    )
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddStudentScreenPreview() {
    AddStudentScreen(
        modifier = Modifier,
        onBack = {},
        onSaveSuccess = {}
    )
}