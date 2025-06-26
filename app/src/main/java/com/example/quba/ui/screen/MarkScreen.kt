package com.example.quba.ui.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quba.loc
import com.example.quba.sub
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Colors
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)
private val DisabledColor = Color(0xFFADB5BD)

// Default values for loc and sub

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
    val subjects = remember(selectedClass) { getSubjectsForClass(selectedClass) }
    val subjectMarks = remember { mutableStateMapOf<String, Pair<Int, Int>>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoadingGenerate by remember { mutableStateOf(false) }
    var isLoadingClear by remember { mutableStateOf(false) }
    val isFormValid = studentName.isNotBlank() && fatherName.isNotBlank() &&
            rollNo.isNotBlank() && date.isNotBlank() &&
            subjects.all { subjectMarks[it]?.let { (hy, an) -> hy in 0..50 && an in 0..50 } == true }

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
                            label = { Text("Student Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Student Name input field" },
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let {
                                { Text(it, color = ErrorColor) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f)
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
                            label = { Text("Father's Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Father's Name input field" },
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let {
                                { Text(it, color = ErrorColor) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f)
                            )
                        )

                        Box(modifier = Modifier.fillMaxWidth()) {
                            var expanded by remember { mutableStateOf(false) }
                            OutlinedTextField(
                                value = selectedClass,
                                onValueChange = {},
                                label = { Text("Class") },
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
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                    disabledBorderColor = TextColor.copy(alpha = 0.2f),
                                    focusedLabelColor = PrimaryColor,
                                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f),
                                    disabledLabelColor = TextColor.copy(alpha = 0.8f)
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
                                        text = { Text(className) },
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
                            label = { Text("Roll No") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Roll Number input field" },
                            isError = errorMessage != null,
                            supportingText = errorMessage?.let {
                                { Text(it, color = ErrorColor) }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                                cursorColor = PrimaryColor,
                                focusedLabelColor = PrimaryColor,
                                unfocusedLabelColor = TextColor.copy(alpha = 0.8f)
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
                            color = TextColor
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
                        isError = errorMessage != null
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
                                MainScope().launch {
                                    delay(1000) // Simulate PDF generation
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
                            )
                        ) {
                            if (isLoadingGenerate) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("GENERATE")
                            }
                        }

                        Button(
                            onClick = {
                                isLoadingClear = true
                                MainScope().launch {
                                    delay(1000) // Simulate clearing
                                    studentName = ""
                                    fatherName = ""
                                    rollNo = ""
                                    date = ""
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
                            )
                        ) {
                            if (isLoadingClear) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("CLEAR")
                            }
                        }
                    }

                    Text(
                        text = "Need help?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = PrimaryColor,
                            fontWeight = FontWeight.Medium
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
        label = { Text("Date") },
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                DatePickerDialog(
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
                }.show()
            }
            .semantics { contentDescription = "Date input field" },
        readOnly = true,
        isError = isError,
        supportingText = if (isError) { { Text("Please select a date", color = ErrorColor) } } else null,
        trailingIcon = {
            IconButton(onClick = {
                DatePickerDialog(
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
                }.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Open date picker",
                    tint = TextColor.copy(alpha = 0.6f)
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryColor,
            unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
            cursorColor = PrimaryColor,
            focusedLabelColor = PrimaryColor,
            unfocusedLabelColor = TextColor.copy(alpha = 0.8f)
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
    isError: Boolean
) {
    var halfYearly by remember { mutableStateOf(subjectMarks[subject]?.first?.toString() ?: "") }
    var annual by remember { mutableStateOf(subjectMarks[subject]?.second?.toString() ?: "") }
    var hyError by remember { mutableStateOf<String?>(null) }
    var annualError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(subject) {
        if (!subjectMarks.containsKey(subject)) {
            subjectMarks[subject] = Pair(0, 0)
        }
    }

    LaunchedEffect(halfYearly) {
        if (halfYearly.isNotEmpty()) {
            val value = halfYearly.toIntOrNull() ?: 0
            hyError = if (value > 50) "Max 50" else null
            onMarksChanged(value, annual.toIntOrNull() ?: 0)
        }
    }

    LaunchedEffect(annual) {
        if (annual.isNotEmpty()) {
            val value = annual.toIntOrNull() ?: 0
            annualError = if (value > 50) "Max 50" else null
            onMarksChanged(halfYearly.toIntOrNull() ?: 0, value)
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
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .semantics { contentDescription = "$subject label" },
                textAlign = TextAlign.Start
            )

            OutlinedTextField(
                value = halfYearly,
                onValueChange = { input ->
                    if (input.length <= 2 && (input.isEmpty() || input.toIntOrNull() != null)) {
                        halfYearly = input
                        if (input.length == 2) ("")
                    }
                },
                label = { Text("HY") },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .semantics { contentDescription = "$subject Half Yearly marks input" },
                isError = isError || hyError != null,
                supportingText = hyError?.let { { Text(it, color = ErrorColor) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f)
                )
            )

            OutlinedTextField(
                value = annual,
                onValueChange = { input ->
                    if (input.length <= 2 && (input.isEmpty() || input.toIntOrNull() != null)) {
                        annual = input
                        if (input.length == 2) ("")
                    }
                },
                label = { Text("AN") },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .semantics { contentDescription = "$subject Annual marks input" },
                isError = isError || annualError != null,
                supportingText = annualError?.let { { Text(it, color = ErrorColor) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (subjectIndex < totalSubjects - 1) ImeAction.Next else ImeAction.Done
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = TextColor.copy(alpha = 0.2f),
                    cursorColor = PrimaryColor,
                    focusedLabelColor = PrimaryColor,
                    unfocusedLabelColor = TextColor.copy(alpha = 0.8f)
                )
            )
        }
    }
}

fun getSubjectsForClass(className: String): List<String> {
    return when (className) {
        "Nur" -> listOf("Urdu", "Hindi", "English", "Arabic", "Mathematics", "Counting", "Dua")
        "KG" -> listOf("Urdu", "Hindi", "English", "Drawing", "Arabic", "Mathematics", "Dua")
        "1st" -> listOf("Urdu", "Hindi", "English", "Diniyat", "Arabic", "Mathematics", "Dua")
        "2nd", "3rd", "4th", "5th" -> listOf(
            "Urdu", "Hindi", "English", "Diniyat", "Arabic",
            "Mathematics", "Science", "Social Study", "Moral Education", "Dua"
        )
        else -> listOf("Urdu", "Hindi", "English", "Diniyat", "Arabic", "Mathematics", "Dua")
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MarkScreenPreview() {
    MaterialTheme {
        MarkScreen()
    }
}