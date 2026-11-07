package com.example.gymud.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymud.screens.common.*

@Composable
fun ForgotPasswordScreen(
    onSendResetClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    isLoading: Boolean = false
) {
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(GymudDark).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Recuperar Cuenta", "Ingresa tu correo asociado")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico", color = Color.Gray) },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = GymudYellow,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Button(
                onClick = { onSendResetClick(email) },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text("Enviar enlace", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateBack, enabled = !isLoading) {
            Text("Volver al inicio de sesión", color = GymudYellow, fontWeight = FontWeight.Bold)
        }
    }
}