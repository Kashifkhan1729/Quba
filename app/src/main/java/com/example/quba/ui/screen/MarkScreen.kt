

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import java.util.Locale
import android.app.DatePickerDialog
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {} // Added for navigation consistency
) {
    val classes = listOf("Nursery", "KG", "1st", "2nd", "3rd", "4th", "5th")
    var studentName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var rollNo by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("Nursery") }
    var date by remember { mutableStateOf("") }
    val subjects = getSubjectsForClass(selectedClass)
    val subjectMarks = remember { mutableStateMapOf<String, Pair<Int, Int>>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isFormValid = studentName.isNotBlank() && fatherName.isNotBlank() && rollNo.isNotBlank() && date.isNotBlank() && subjects.all { subjectMarks[it]?.let { (hy, an) -> hy >= 0 && an >= 0 } == true }
    if (loc==1){
        selectedClass=sub
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
                                text = "Student Marks Entry",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 24.sp,
                                    color = Color.DarkGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                                    .semantics { contentDescription = "Student Marks Entry Title" },
                                textAlign = TextAlign.Center
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

                            // Class Dropdown
                            var expanded by remember { mutableStateOf(false) }
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = if (loc == 1) sub else selectedClass,
                                    onValueChange = {},
                                    label = { Text("Class Name", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)) },

                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .semantics { contentDescription = "Class selection field" },
                                    readOnly = true,
                                    trailingIcon = { if (loc!=1){
                                        IconButton(onClick = { expanded = !expanded },
                                            enabled = loc==0
                                        ) {
                                            Icon(
                                                imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                                contentDescription = "Toggle class dropdown",
                                                tint = if (loc == 1)Color.DarkGray else Color.DarkGray
                                            )
                                        }}
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SoftBlue,
                                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                                        errorBorderColor = MaterialTheme.colorScheme.error
                                    )
                                )
                                if (loc!=0){
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
                                    classes.forEach { className ->
                                        DropdownMenuItem(
                                            text = { Text(className, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp, color = Color.DarkGray)) },
                                            onClick = {
                                                selectedClass = className
                                                expanded = false
                                                errorMessage = null
                                                subjectMarks.clear() // Reset marks when class changes
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (selectedClass == className) SoftBlue.copy(alpha = 0.1f) else Color.Transparent
                                                )
                                                .semantics { contentDescription = "Select $className" }
                                        )
                                    }
                                }}
                            }

                            CustomTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = rollNo,
                                onValueChange = {
                                    rollNo = it
                                    errorMessage = null
                                },
                                label = "Roll No",
                                contentDescription = "Roll Number input field",
                                isError = errorMessage != null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                }

                item {
                    Text(
                        text = "Subject Marks",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.DarkGray
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
                        subjectMarks = subjectMarks,
                        onMarksChanged = { halfYearly, annual ->
                            subjectMarks[subjects[index]] = Pair(halfYearly, annual)
                            errorMessage = null
                        },
                        isError = errorMessage != null
                    )
                }

                item {
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
                                errorMessage = if (isFormValid) {
                                    "PDF generation not implemented"
                                } else {
                                    "Please fill all fields and enter valid marks"
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
                                .semantics { contentDescription = "Generate PDF button" },
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
                                text = "Generate PDF",
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
                                fatherName = ""
                                rollNo = ""
                                date = ""
                                subjectMarks.clear()
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
                                text = "Clear Form",
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
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
fun DatePickerField(
    modifier: Modifier = Modifier,
    date: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("26/06/2025", Locale.getDefault())

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
            }
            datePickerDialog.show()
        },
        value = date,
        onValueChange = {},
        label = "Date of Birth",
        contentDescription = "Date input field",
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

@Composable
fun SubjectMarksInput(
    modifier: Modifier = Modifier,
    subject: String,
    subjectMarks: SnapshotStateMap<String, Pair<Int, Int>>,
    onMarksChanged: (Int, Int) -> Unit,
    isError: Boolean
) {
    var halfYearly by remember { mutableStateOf("") }
    var annual by remember { mutableStateOf("") }

    Row(
        modifier = modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = subject,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.DarkGray
            ),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .semantics { contentDescription = "$subject label" },
            textAlign = TextAlign.Start
        )

        CustomTextField(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            value = halfYearly,
            onValueChange = {
                halfYearly = it
                onMarksChanged(it.toIntOrNull() ?: 0, annual.toIntOrNull() ?: 0)
            },
            label = "HY",
            contentDescription = "$subject Half Yearly marks input",
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        CustomTextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            value = annual,
            onValueChange = {
                annual = it
                onMarksChanged(halfYearly.toIntOrNull() ?: 0, it.toIntOrNull() ?: 0)
            },
            label = "Annual",
            contentDescription = "$subject Annual marks input",
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}


fun getSubjectsForClass(className: String): List<String> {
    return when (className) {
        "Nursery" -> listOf("English", "Maths", "Drawing")
        "KG" -> listOf("English", "Maths", "EVS", "Drawing")
        else -> listOf("English", "Maths", "Science", "Social Studies", "Hindi")
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MarkScreenPreview() {
    MarkScreen()
}