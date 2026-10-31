package com.example.gymud.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*

@Composable
fun UserHomeScreen(
    userName: String,
    onNavigateToRoutine: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToMembership: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp).verticalScroll(rememberScrollState())) {
        ScreenTitle("BIENVENIDO,\n${userName.uppercase()}!", "Aquí está tu resumen de hoy")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("Snapshot de Fitness", color = GymudYellow, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
            Text("Próximo Entrenamiento: Por asignar", color = Color.White)
            Text("Porcentaje de Grasa: Sin registrar", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = 0f, color = GymudYellow, trackColor = Color.DarkGray, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Accesos Rápidos", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        // Primera fila de botones
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onNavigateToRoutine, colors = ButtonDefaults.buttonColors(containerColor = GymudSurface), modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = GymudYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rutina", color = Color.White)
            }
            Button(onClick = onNavigateToProgress, colors = ButtonDefaults.buttonColors(containerColor = GymudSurface), modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = GymudYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Progreso", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onNavigateToMembership, colors = ButtonDefaults.buttonColors(containerColor = GymudSurface), modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = GymudYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Plan", color = Color.White)
            }
            Button(onClick = onNavigateToChat, colors = ButtonDefaults.buttonColors(containerColor = GymudSurface), modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Chat, contentDescription = null, tint = GymudYellow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Entrenador", color = Color.White)
            }
        }
    }
}