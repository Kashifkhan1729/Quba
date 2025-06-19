package com.example.quba.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Colors from AdminScreen
private val SecondaryColor = Color(0xFF3F37C9)
private val BackgroundColor = Color(0xFFF8F9FA)
private val CardColor = Color.White
private val TextColor = Color(0xFF212529)

private val DisabledColor = Color(0xFFADB5BD)

@Composable
fun MainScreen(
    onAdminClick: () -> Unit,
    onTeacherClick: () -> Unit,
    onStudentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimaryColor.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SupervisorAccount,
                            contentDescription = "Role selection icon",
                            tint = PrimaryColor,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Text(
                        text = "Choose Your Role",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextColor,
                            fontFamily = FontFamily.SansSerif
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics { contentDescription = "Choose Your Role Title" }
                    )

                    Text(
                        text = "Select your role to proceed",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextColor.copy(alpha = 0.6f),
                            fontFamily = FontFamily.SansSerif
                        )
                    )
                }

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
                        modifier = Modifier.fillMaxWidth()
                    )
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
    var isLoading by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed && !isLoading) PrimaryColor.copy(alpha = 0.8f) else PrimaryColor,
        animationSpec = tween(durationMillis = 200)
    )

    Button(
        onClick = {
            isLoading = true
            kotlinx.coroutines.MainScope().launch {
                delay(100) // Simulate navigation
                onClick()
                isLoading = false
            }
        },
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(12.dp))
            .semantics { this.contentDescription = contentDescription },
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White,
            disabledContainerColor = DisabledColor,
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        interactionSource = interactionSource
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = "Icon for $text role",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, widthDp = 720, heightDp = 1280, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(
            onAdminClick = {},
            onTeacherClick = {},
            onStudentClick = {}
        )
    }
}