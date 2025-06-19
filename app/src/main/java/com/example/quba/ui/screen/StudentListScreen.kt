package com.example.quba.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quba.utils.StudentViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.quba.*
import kotlinx.coroutines.delay

// Colors from AdminScreen
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)

private val DisabledColor = Color(0xFFADB5BD)

data class Student(val id: Int, val name: String, val classname: String)

@Composable
fun StudentListScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onMarkSheet: (String) -> Unit,
    onAdd: () -> Unit,
    onEdit: () -> Unit,
    viewModel: StudentViewModel = viewModel()
) {
    val students by viewModel.students.collectAsState()
    var selectedClass by remember { mutableStateOf(sub) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var isLoadingMarkSheet by remember { mutableStateOf(false) }
    var isLoadingAdd by remember { mutableStateOf(false) }
    var isLoadingEdit by remember { mutableStateOf(false) }
    var isLoadingList by remember { mutableStateOf(true) }
    var studentToDelete by remember { mutableStateOf<Student?>(null) }
    val classList = listOf("Nur", "KG", "1st", "2nd", "3rd", "4th", "5th")

    // Fetch students when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchStudents()
        isLoadingList = false
    }

    // Filter students based on selected class
    val filteredStudents = if (loc == 0) {
        students.filter { it.classname == selectedClass }
    } else {
        students.filter { it.classname == sub }.also {
            selectedClass = sub // Ensure selectedClass is always sub when loc is 1
        }
    }

    // Delete confirmation dialog
    studentToDelete?.let { student ->
        Dialog(
            onDismissRequest = { studentToDelete = null }
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
                        text = "Delete ${student.name}?",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Text(
                        text = "This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { studentToDelete = null },
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
                                viewModel.deleteStudent(student)
                                studentToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ErrorColor,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Delete")
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .semantics { contentDescription = "Back to admin dashboard" }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = TextColor
                            )
                        }

                        Text(
                            text = "Student List",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextColor,
                                fontFamily = FontFamily.SansSerif
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                                .semantics { contentDescription = "Student List Title" }
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
                            imageVector = Icons.Filled.Group,
                            contentDescription = "Student list icon",
                            tint = PrimaryColor,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "Manage student records",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
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
                                    .clickable { if (loc == 0) isDropdownExpanded = true }
                                    .semantics { contentDescription = "Class selection field" },
                                readOnly = true,
                                enabled = loc == 0,
                                trailingIcon = {
                                    if (loc == 0) {
                                        IconButton(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                                            Icon(
                                                imageVector = if (isDropdownExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
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

                            if (loc == 0) {
                                DropdownMenu(
                                    expanded = isDropdownExpanded,
                                    onDismissRequest = { isDropdownExpanded = false },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CardColor)
                                ) {
                                    classList.forEach { className ->
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
                                                sub = className
                                                isDropdownExpanded = false
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
                        }

                        Button(
                            onClick = {
                                isLoadingMarkSheet = true
                                kotlinx.coroutines.MainScope().launch {
                                    delay(10) // Simulate action
                                    onMarkSheet(selectedClass)
                                    isLoadingMarkSheet = false
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .semantics { contentDescription = "View MarkSheet" },
                            enabled = selectedClass.isNotEmpty() && !isLoadingMarkSheet,
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
                            if (isLoadingMarkSheet) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    text = "MARKSHEET",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 1.sp,
                                        fontFamily = FontFamily.SansSerif
                                    )
                                )
                            }
                        }
                    }

                    if (isLoadingList) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = PrimaryColor,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    } else if (filteredStudents.isEmpty()) {
                        Text(
                            text = "No students found for $selectedClass",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextColor.copy(alpha = 0.6f),
                                fontFamily = FontFamily.SansSerif,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredStudents, key = { it.id }) { student ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onEdit() }
                                        .semantics { contentDescription = "Student ${student.name}" },
                                    colors = CardDefaults.cardColors(containerColor = CardColor),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${student.name} (${student.classname})",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Medium,
                                                color = TextColor,
                                                fontFamily = FontFamily.SansSerif
                                            )
                                        )
                                        IconButton(
                                            onClick = { studentToDelete = student },
                                            modifier = Modifier.semantics { contentDescription = "Delete ${student.name}" }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Delete,
                                                contentDescription = null,
                                                tint = PrimaryColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            isLoadingAdd = true
                            kotlinx.coroutines.MainScope().launch {
                                delay(1000) // Simulate action
                                onAdd()
                                isLoadingAdd = false
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .semantics { contentDescription = "Add student" },
                        enabled = !isLoadingAdd,
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
                        if (isLoadingAdd) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "ADD",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        }
                    }

                    Button(
                        onClick = {
                            isLoadingEdit = true
                            kotlinx.coroutines.MainScope().launch {
                                delay(1000) // Simulate action
                                onEdit()
                                isLoadingEdit = false
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .semantics { contentDescription = "Edit student" },
                        enabled = filteredStudents.isNotEmpty() && !isLoadingEdit,
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
                        if (isLoadingEdit) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "EDIT",
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
                        .semantics { contentDescription = "Need help link" }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, widthDp = 720, heightDp = 1280, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentListScreenPreview() {
    MaterialTheme {
        StudentListScreen(
            onBack = {},
            onMarkSheet = {},
            onAdd = {},
            onEdit = {}
        )
    }
}