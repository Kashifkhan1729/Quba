package com.example.quba.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quba.ui.screen.Student
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    fun fetchStudents() {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("students").get().await()
                val studentList = snapshot.documents.map { doc ->
                    Student(
                        id = doc.getLong("rollno")?.toInt() ?: 0,
                        name = doc.getString("name") ?: "",
                        className = doc.getString("className") ?: ""
                    )
                }
                _students.value = studentList
            } catch (e: Exception) {
                // Handle error
                println("Error fetching students: ${e.message}")
            }
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            try {
                db.collection("students")
                    .whereEqualTo("id", student.id)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("students").document(document.id).delete()
                        }
                        fetchStudents() // Refresh the list after deletion
                    }
            } catch (e: Exception) {
                // Handle error
                println("Error deleting student: ${e.message}")
            }
        }
    }
}