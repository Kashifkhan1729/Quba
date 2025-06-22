package com.example.quba.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
//import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.DatePickerDialog
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.quba.loc
import com.example.quba.sub
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Colors from AdminScreen
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)

private val DisabledColor = Color(0xFFADB5BD)

@Composable
fun MarkScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val classes = listOf("Nur", "KG", "1st", "2nd", "3rd", "4th", "5th")
    var studentName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf(if (loc == 1) sub else "Nur") }
    var date by remember { mutableStateOf("") }
    val subjects = getSubjectsForClass(selectedClass)
    val subjectMarks = remember { mutableStateMapOf<String, Pair<Int, Int>>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoadingGenerate by remember { mutableStateOf(false) }
    var isLoadingClear by remember { mutableStateOf(false) }
    val isFormValid = studentName.isNotBlank() && fatherName.isNotBlank() &&
            rollNo.isNotBlank() && date.isNotBlank() &&
            subjects.all { subjectMarks[it]?.let { (hy, an) -> hy in 0..50 && an in 0..50 } == true }
    val focusRequesters = remember { subjects.map { FocusRequester() } }
    val focusManager = LocalFocusManager.current

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
                                text = "Student Marks",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextColor,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)
                                    .semantics { contentDescription = "Student Marks Entry Title" }
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
                                imageVector = Icons.AutoMirrored.Filled.Assignment,
                                contentDescription = "Marks entry icon",
                                tint = PrimaryColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Text(
                            text = "Enter student marks and generate report",
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
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let {
                                { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) }
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
                                disabledContainerColor = Color.Transparent,
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
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let {
                                { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) }
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
                                disabledContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            var expanded by remember { mutableStateOf(false) }
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
                                    .clickable(enabled = loc == 0) { expanded = true }
                                    .semantics { contentDescription = "Class selection field" },
                                readOnly = true,
                                enabled = loc == 0,
                                trailingIcon = {
                                    if (loc == 0) {
                                        IconButton(onClick = { expanded = !expanded }) {
                                            Icon(
                                                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                contentDescription = "Toggle class dropdown",
                                                tint = TextColor.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                    disabledBorderColor = TextColor.copy(alpha = 0.2f),
                                    cursorColor = PrimaryColor,
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                    disabledLabelColor = TextColor.copy(alpha = 0.8f),
                                    focusedTextColor = TextColor,
                                    unfocusedTextColor = TextColor,
                                    disabledTextColor = TextColor
                                )
                            )

                            DropdownMenu(
                                expanded = expanded && loc == 0,
                                onDismissRequest = { expanded = false },
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
                                            expanded = false
                                            errorMessage = null
                                            subjectMarks.clear()
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
                            value = rollNo,
                            onValueChange = {
                                if (it.length <= 10 && (it.isEmpty() || it.matches(Regex("^[0-9]*$")))) {
                                    rollNo = it
                                    errorMessage = null
                                }
                            },
                            label = {
                                Text(
                                    "Roll No",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextColor.copy(alpha = 0.8f),
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Roll Number input field" },
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let {
                                { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) }
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
                                disabledContainerColor = Color.Transparent,
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                focusedTextColor = TextColor,
                                unfocusedTextColor = TextColor
                            )
                        )

                        DatePickerField(
                            modifier = Modifier.fillMaxWidth(),
                            date = date,
                            onDateSelected = {
                                date = it
                                errorMessage = null
                            },
                            isError = errorMessage != null
                        )
                    }
                }

                item {
                    Text(
                        text = "Subject Marks",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = FontFamily.SansSerif
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp)
                            .semantics { contentDescription = "Subject Marks Section" },
                        textAlign = TextAlign.Center
                    )
                }

                items(subjects.size) { index ->
                    SubjectMarksInput(
                        modifier = Modifier.fillMaxWidth(),
                        subject = subjects[index],
                        subjectIndex = index,
                        totalSubjects = subjects.size,
                        subjectMarks = subjectMarks,
                        onMarksChanged = { halfYearly, annual ->
                            subjectMarks[subjects[index]] = Pair(halfYearly, annual)
                            errorMessage = null
                        },
                        isError = errorMessage != null,
                        focusRequester = focusRequesters[index],
                        nextFocusRequester = focusRequesters.getOrNull(index + 1),
                        onHyFilled = {
                            if (index < subjects.size) {
                                focusRequesters[index].requestFocus()
                            }
                        },
                        onAnnualFilled = {
                            if (index < subjects.size - 1) {
                                focusRequesters[index + 1].requestFocus()
                            } else {
                                focusManager.clearFocus()
                            }
                        }
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                isLoadingGenerate = true
                                kotlinx.coroutines.MainScope().launch {
                                    delay(10) // Simulate PDF generation
                                    errorMessage = if (isFormValid) {
                                        "PDF generation not implemented"
                                    } else {
                                        "Please fill all fields and enter valid marks"
                                    }
                                    isLoadingGenerate = false
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .semantics { contentDescription = "Generate PDF button" },
                            enabled = isFormValid && !isLoadingGenerate,
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
                            if (isLoadingGenerate) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "GENERATE",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 1.sp,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = {
                                isLoadingClear = true
                                kotlinx.coroutines.MainScope().launch {
                                    delay(10) // Simulate clearing
                                    studentName = ""
                                    fatherName = ""
                                    rollNo = ""
//                                    date = ""
                                    subjectMarks.clear()
                                    errorMessage = null
                                    isLoadingClear = false
                                }
                            },
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
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "CLEAR",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Medium,
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

@Composable
fun DatePickerField(
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
                "Date",
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
                }
                datePickerDialog.show()
            }
            .semantics { contentDescription = "Date input field" },
        readOnly = true,
        isError = isError,
        supportingText = isError.let { { Text("Please select a date", color = ErrorColor, fontFamily = FontFamily.SansSerif) } },
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
            disabledContainerColor = Color.Transparent,
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

@Composable
fun SubjectMarksInput(
    modifier: Modifier = Modifier,
    subject: String,
    subjectIndex: Int,
    totalSubjects: Int,
    subjectMarks: SnapshotStateMap<String, Pair<Int, Int>>,
    onMarksChanged: (Int, Int) -> Unit,
    isError: Boolean,
    focusRequester: FocusRequester,
    nextFocusRequester: FocusRequester?,
    onHyFilled: () -> Unit,
    onAnnualFilled: () -> Unit
) {
    var halfYearly by remember { mutableStateOf(subjectMarks[subject]?.first?.toString() ?: "") }
    var annual by remember { mutableStateOf(subjectMarks[subject]?.second?.toString() ?: "") }
    var hyError by remember { mutableStateOf<String?>(null) }
    var annualError by remember { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    // Update subjectMarks when the composable is initialized
    LaunchedEffect(subject) {
        if (!subjectMarks.containsKey(subject)) {
            subjectMarks[subject] = Pair(0, 0) // Initialize with default values
        }
    }

    LaunchedEffect(halfYearly) {
        if (halfYearly.isNotEmpty()) {
            val value = halfYearly.toIntOrNull() ?: 0
            if (value > 50) {
                hyError = "Max 50"
            } else {
                hyError = null
                if (halfYearly.length == 2) {
                    // Only request focus if nextFocusRequester is valid
                    nextFocusRequester?.requestFocus()
                    onHyFilled()
                }
            }
            onMarksChanged(halfYearly.toIntOrNull() ?: 0, annual.toIntOrNull() ?: 0)
        }
    }

    LaunchedEffect(annual) {
        if (annual.isNotEmpty()) {
            val value = annual.toIntOrNull() ?: 0
            if (value > 50) {
                annualError = "Max 50"
            } else {
                annualError = null
                if (annual.length == 2) {
                    onAnnualFilled()
                }
            }
            onMarksChanged(halfYearly.toIntOrNull() ?: 0, annual.toIntOrNull() ?: 0)
        }
    }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = subject,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = TextColor,
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 5.dp)
                    .semantics { contentDescription = "$subject label" },
                textAlign = TextAlign.Start
            )

            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = halfYearly,
                    onValueChange = { input ->
                        if (input.length <= 2 && (input.isEmpty() || input.toIntOrNull() != null)) {
                            halfYearly = input
                        }
                    },
                    label = {
                        Text(
                            "HY",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextColor.copy(alpha = 0.8f),
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .focusRequester(focusRequester)
                        .semantics { contentDescription = "$subject Half Yearly marks input" },
                    isError = isError || hyError != null,
                    supportingText = hyError?.let {
                        { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (annual.isEmpty()) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { nextFocusRequester?.requestFocus() },
                        onDone = { focusManager.clearFocus() }
                    ),
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
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = annual,
                    onValueChange = { input ->
                        if (input.length <= 2 && (input.isEmpty() || input.toIntOrNull() != null)) {
                            annual = input
                        }
                    },
                    label = {
                        Text(
                            "AN",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextColor.copy(alpha = 0.8f),
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                        .semantics { contentDescription = "$subject Annual marks input" },
                    isError = isError || annualError != null,
                    supportingText = annualError?.let {
                        { Text(it, color = ErrorColor, fontFamily = FontFamily.SansSerif) }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (subjectIndex < totalSubjects - 1) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { nextFocusRequester?.requestFocus() },
                        onDone = { focusManager.clearFocus() }
                    ),
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
                    )
                )
            }
        }
    }
}

fun getSubjectsForClass(className: String): List<String> {
    return when (className) {
        "Nursery" -> listOf("Urdu", "Hindi", "English", "Arabic", "Mathematics", "Counting", "Dua")
        "KG" -> listOf("Urdu", "Hindi", "English", "Drawing", "Arabic", "Mathematics", "Dua")
        "1st" -> listOf("Urdu", "Hindi", "English", "Diniyat", "Arabic", "Mathematics", "Dua")
        "2nd" -> listOf(
            "Urdu", "Hindi", "English", "Diniyat", "Arabic",
            "Mathematics", "Science", "Social Study", "Moral Education", "Dua"
        )
        else -> listOf(
            "Urdu", "Hindi", "English", "Diniyat", "Arabic",
            "Mathematics", "Science", "Social Study", "Moral Education", "Dua"
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
//@Preview(showBackground = true, widthDp = 720, heightDp = 1280, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MarkScreenPreview() {
    MaterialTheme {
        MarkScreen()
    }
}