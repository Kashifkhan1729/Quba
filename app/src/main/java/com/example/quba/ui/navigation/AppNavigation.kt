

package com.example.quba.ui.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.quba.ui.screen.AddStudentScreen
import com.example.quba.ui.screen.AdminScreen
import com.example.quba.ui.screen.MainScreen
import com.example.quba.ui.screen.MarkScreen
import com.example.quba.ui.screen.StudentListScreen
import com.example.quba.ui.screen.StudentScreen
import com.example.quba.ui.screen.TeacherScreen

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    var teacherSelectedClass by remember { mutableStateOf("") }

    when (currentScreen) {
        Screen.Main -> MainScreen(
            onAdminClick = { currentScreen = Screen.Admin },
            onTeacherClick = { currentScreen = Screen.Teacher },
            onStudentClick = { currentScreen = Screen.Student }
        )
        Screen.Admin -> AdminScreen(
            onBack = { currentScreen = Screen.Main },
            onLoginSuccess = { currentScreen = Screen.List },
            modifier = Modifier
        )
        Screen.Teacher -> TeacherScreen(
            onBack = { currentScreen = Screen.Main },
            onLoginSuccess = { className ->
                teacherSelectedClass = className
                currentScreen = Screen.List
            }
        )
        Screen.Student -> StudentScreen(
            onBack = { currentScreen = Screen.Main },
            onLogin = { currentScreen = Screen.Main }
        )

        Screen.List -> StudentListScreen(
            modifier = Modifier,
            onBack = {currentScreen=Screen.Main},
            onMarkSheet = {currentScreen=Screen.Marks},
            onAdd = {currentScreen=Screen.Add},
            onEdit = {currentScreen=Screen.Main}
        )

        Screen.Marks -> MarkScreen(
            modifier = Modifier,
            onBack = {currentScreen = Screen.List}
                   )
        Screen.Add -> AddStudentScreen(
            modifier = Modifier,
            onBack = { currentScreen = Screen.List },
            onSaveSuccess = {currentScreen= Screen.Add}
        )
    }
}

sealed class Screen {
    object Main : Screen()
    object Admin : Screen()
    object Teacher : Screen()
    object Student : Screen()
    object List : Screen()
    object Marks : Screen()
    object Add  : Screen()

}