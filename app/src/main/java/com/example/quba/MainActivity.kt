
package com.example.quba
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.quba.ui.navigation.AppNavigation
import com.example.quba.ui.theme.QubaTheme
import com.example.quba.utils.kashif
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QubaApp()
            kashif()
        }
    }
}

var loc: Int =0
var sub: String = "Nur"

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
