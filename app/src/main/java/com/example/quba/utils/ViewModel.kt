package com.example.quba.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quba.ui.screen.Student
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class StudentViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    fun fetchStudents() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("students").get().await()
                if (snapshot.isEmpty) {
                    println("No students found in Firestore collection 'students'")
                    _students.value = emptyList()
                } else {
                    val studentList = snapshot.documents.map { doc ->
                        Student(
                            rollno = doc.getLong("rollno")?.toInt() ?: 0,
                            name = doc.getString("name"),
                            classname = doc.getString("className"),
                            dateOfBirth = doc.getString("dateOfBirth"),
                            gender = doc.getString("gender"),
                            nationality = doc.getString("nationality"),
                            aadhaarNumber = doc.getString("aadhaarNumber"),
                            fatherName = doc.getString("fatherName"),
                            motherName = doc.getString("motherName"),
                            guardianName = doc.getString("guardianName"),
                            contactNumber = doc.getString("contactNumber"),
                            emailAddress = doc.getString("emailAddress"),
                            fatherOccupation = doc.getString("fatherOccupation"),
                            motherOccupation = doc.getString("motherOccupation"),
                            address = doc.getString("address"),
                            previousSchoolName = doc.getString("previousSchoolName"),
                            previousSchoolAddress = doc.getString("previousSchoolAddress"),
                            category = doc.getString("category"),
                            religion = doc.getString("religion"),
                            motherTongue = doc.getString("motherTongue"),
                            siblingsInSchool = doc.getString("siblingsInSchool"),
                            transport = doc.getString("transport")
                        )
                    }
                    println("Fetched students from Firestore: $studentList")
                    _students.value = studentList
                }
            } catch (e: Exception) {
                println("Error fetching students: ${e.message}")
                _students.value = emptyList()
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            try {
                val querySnapshot = db.collection("students")
                    .whereEqualTo("rollno", student.rollno.toLong())
                    .get()
                    .await()
                for (document in querySnapshot.documents) {
                    db.collection("students").document(document.id).delete().await()
                    println("Deleted student with rollno: ${student.rollno}")
                }
                fetchStudents()
            } catch (e: Exception) {
                println("Error deleting student: ${e.message}")
            }
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            try {
                // Generate rollno
                val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
                val classCode = when (student.classname) {
                    "Nur", "KG" -> "00"
                    "1st" -> "01"
                    "2nd" -> "02"
                    "3rd" -> "03"
                    "4th" -> "04"
                    "5th" -> "05"
                    else -> "00" // Default to 00 for unknown classes
                }

                // Count existing students in the same class for the current year
                val snapshot = db.collection("students")
                    .whereEqualTo("className", student.classname)
                    .whereGreaterThanOrEqualTo("rollno", (currentYear + classCode + "000").toLong())
                    .whereLessThanOrEqualTo("rollno", (currentYear + classCode + "999").toLong())
                    .get()
                    .await()

                val sequenceNumber = (snapshot.documents.size + 1).toString().padStart(3, '0')
                val rollno = (currentYear + classCode + sequenceNumber).toLong()

                // Prepare student data for Firestore
                val studentData = hashMapOf(
                    "rollno" to rollno,
                    "name" to student.name,
                    "className" to student.classname,
                    "dateOfBirth" to student.dateOfBirth,
                    "gender" to student.gender,
                    "nationality" to student.nationality,
                    "aadhaarNumber" to student.aadhaarNumber,
                    "fatherName" to student.fatherName,
                    "motherName" to student.motherName,
                    "guardianName" to student.guardianName,
                    "contactNumber" to student.contactNumber,
                    "emailAddress" to student.emailAddress,
                    "fatherOccupation" to student.fatherOccupation,
                    "motherOccupation" to student.motherOccupation,
                    "address" to student.address,
                    "previousSchoolName" to student.previousSchoolName,
                    "previousSchoolAddress" to student.previousSchoolAddress,
                    "category" to student.category,
                    "religion" to student.religion,
                    "motherTongue" to student.motherTongue,
                    "siblingsInSchool" to student.siblingsInSchool,
                    "transport" to student.transport
                ).filterValues { it != null } // Remove null values to avoid Firestore issues

                // Save to Firestore
                db.collection("students").document(rollno.toString()).set(studentData).await()
                println("Added student with rollno: $rollno")
                fetchStudents() // Refresh the list
            } catch (e: Exception) {
                println("Error adding student: ${e.message}")
            }
        }
    }
}