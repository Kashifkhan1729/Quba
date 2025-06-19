package com.example.quba.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.quba.utils.StudentViewModel
import com.example.quba.*
import com.example.quba.ui.theme.*


data class Student(val id: Int, val name: String, val className: String)
@Composable
fun StudentListScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onMarkSheet: (String) -> Unit,
    onAdd: () -> Unit,
    onEdit: () -> Unit,
) {
    val viewModel: StudentViewModel = viewModel()
    val students by viewModel.students.collectAsState()

    // Fetch students when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.fetchStudents()
    }

    var selectedClass by remember { mutableStateOf(sub) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val classList = listOf("Nur", "KG", "1st", "2nd", "3rd", "4th", "5th")

    // Filter students based on selected class
    val filteredStudents = students

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SoftGrey)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AccentCream
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row: Back Button, Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SoftBlue.copy(alpha = 0.1f))
                            .semantics { contentDescription = "Back to admin screen" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }

                    Text(
                        text = "Student List",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            color = Color.DarkGray
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .semantics { contentDescription = "Student List Title" }
                    )
                }

                // Row for Dropdown and Mark Sheet Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Dropdown for Class Selection
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = selectedClass,
                                onValueChange = {},
                                label = { Text("Class") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { if (loc == 0) isDropdownExpanded = true },
                                readOnly = true,
                                enabled = false,
                                trailingIcon = {
                                    if (loc == 0) {
                                        Icon(
                                            if (isDropdownExpanded) Icons.Filled.ArrowDropUp
                                            else Icons.Filled.ArrowDropDown,
                                            contentDescription = "Class dropdown"
                                        )
                                    }
                                }
                            )

                            DropdownMenu(
                                expanded = isDropdownExpanded && loc == 0,
                                onDismissRequest = { isDropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                classList.forEach { className ->
                                    DropdownMenuItem(
                                        text = { Text(className) },
                                        onClick = {
                                            selectedClass = className
                                            sub = className
                                            isDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        // MarkSheet Button
                        Button(
                            onClick = { onMarkSheet(selectedClass) },
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(SoftBlue, SoftBlue.copy(alpha = 0.8f))
                                    )
                                )
                                .semantics { contentDescription = "View MarkSheet" },
                            enabled = selectedClass != "",
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.White,
                                disabledContainerColor = SoftBlue.copy(alpha = 0.3f),
                                disabledContentColor = Color.White.copy(alpha = 0.5f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 4.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            Text(
                                text = "MarkSheet",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }}

                // Student List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredStudents) { student ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEdit() }
                                .semantics { contentDescription = "Student ${student.name}" },
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${student.name} (${student.className})",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.DarkGray
                                    )
                                )
                                IconButton(
                                    onClick = { viewModel.deleteStudent(student) },
                                    modifier = Modifier.semantics { contentDescription = "Delete ${student.name}" }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom Buttons: Add, Edit
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = onAdd,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SoftBlue, SoftBlue.copy(alpha = 0.8f))
                                )
                            )
                            .semantics { contentDescription = "Add student" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Button(
                        onClick =  onEdit,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(SoftBlue, SoftBlue.copy(alpha = 0.8f))
                                )
                            )
                            .semantics { contentDescription = "Edit student" },
                        enabled = filteredStudents.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            disabledContainerColor = SoftBlue.copy(alpha = 0.3f),
                            disabledContentColor = Color.White.copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp,
                            disabledElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun StudentListScreenPreview() {
//    val sampleStudents = listOf(
//        Student(1, "John Doe", "1st"),
//        Student(2, "Jane Smith", "2nd"),
//        Student(3, "Alice Johnson", "1st")
//    )
//
//    }
//
//    StudentListScreen(
//        modifier = Modifier,
//        onBack = {},
//        onMarkSheet = {},
//        onAdd = {},
//        onEdit = {},
//    )
//}