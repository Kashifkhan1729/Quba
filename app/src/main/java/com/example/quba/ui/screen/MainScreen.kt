
package com.example.quba.ui.screen
//
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.TweenSpec
//import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color.Companion
//import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
//import com.example.quba.ui.theme.SoftBlue

// Modern palette colors
//private val GradientBlueStart = Color(0xFF5A9DF9)
//private val GradientBlueEnd = Color(0xFF3B6CEC)
private val CardBackground = Color(0x33FFFFFF) // Transparent white for glass effect
private val BackgroundGradientStart = Color(0xFF15202B)
private val BackgroundGradientEnd = Color(0xFF192734)
private val TextPrimary = Color(0xFFE1E8ED)
//private val TextSecondary = Color(0xFF8899A6)
private val ButtonTextColor = Color.White
private val ButtonSurfaceColor = Color(0xFF1DA1F2)
//private val ButtonSurfaceColorDark = Color(0xFF0D71C6)
//private val ButtonShadowColor = Color(0x801DA1F2)
private val CardElevation = 16.dp
private val ButtonElevationDefault = 6.dp
private val ButtonElevationPressed = 12.dp

@Composable
fun MainScreen(
    onAdminClick: () -> Unit,
    onTeacherClick: () -> Unit,
    onStudentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Background gradient with subtle blur for a glassmorphic aesthetic
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundGradientStart, BackgroundGradientEnd)
                )
            )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = CardElevation)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Your Role",
                    fontFamily = FontFamily.SansSerif, // Use modern font like Inter or equivalent
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                )
                val roles = listOf(
                    Role(
                        label = "Admin",
                        icon = Icons.Filled.AdminPanelSettings,
                        onClick = onAdminClick
                    ),
                    Role(
                        label = "Teacher",
                        icon = Icons.Filled.School,
                        onClick = onTeacherClick
                    ),
                    Role(
                        label = "Student",
                        icon = Icons.Filled.Person,
                        onClick = onStudentClick
                    )
                )
                roles.forEach { role ->
                    RoleButton(
                        text = role.label,
                        icon = role.icon,
                        onClick = role.onClick,
                        contentDescription = "${role.label} button",
                        modifier = Modifier.fillMaxWidth(0.8f),
                    )
                }
            }
        }
    }
}

private data class Role(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun RoleButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    // Animate button elevation on press
    //val pressedElevation by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(ButtonElevationDefault) }
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .semantics { this.contentDescription = contentDescription },
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonSurfaceColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = ButtonElevationDefault,
            pressedElevation = ButtonElevationPressed
        ),
        shape = RoundedCornerShape(16.dp),
        //contentPadding = androidx.compose.ui.unit.PaddingValues(horizontal = 20.dp)
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ButtonTextColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = text,
                color = ButtonTextColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onAdminClick = {},
        onTeacherClick = {},
        onStudentClick = {}
    )
}
