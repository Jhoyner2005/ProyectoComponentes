package com.example.gymud.screens.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*

@Composable
fun TrainerHomeScreen(
    trainerName: String,
    onNavigateToAssign: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp).verticalScroll(rememberScrollState())) {
        ScreenTitle("HOLA,\n${trainerName.uppercase()}!", "Panel de Entrenador")

        Text("Gestión de Clientes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onNavigateToAssign,
                colors = ButtonDefaults.buttonColors(containerColor = GymudSurface),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Assignment, contentDescription = null, tint = GymudYellow)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Asignar\nRutinas", color = Color.White, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }

            Button(
                onClick = onNavigateToChat,
                colors = ButtonDefaults.buttonColors(containerColor = GymudSurface),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Chat, contentDescription = null, tint = GymudYellow)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Mensajes\nClientes", color = Color.White, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }
    }
}