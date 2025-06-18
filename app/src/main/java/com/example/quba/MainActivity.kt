
package com.example.quba
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.quba.ui.navigation.AppNavigation
import com.example.quba.ui.theme.QubaTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QubaApp()
            val database = Firebase.database
            val myRef = database.getReference("message")
            myRef.setValue("Hello, World!")
        }
    }
}

@Composable
fun QubaApp() {
    QubaTheme {
        AppNavigation()
    }
}

@Preview(showSystemUi = true)
@Composable
fun QubaAppPreview() {
    QubaApp()
}