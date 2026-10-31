package com.example.gymud.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*

@Composable
fun AdminDashboardScreen(
    onNavigateToUsers: () -> Unit,
    onNavigateToInventory: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp).verticalScroll(rememberScrollState())) {
        ScreenTitle("Panel de Control", "Vista General del Gimnasio")

        // Botones de Navegación del Admin
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onNavigateToUsers,
                colors = ButtonDefaults.buttonColors(containerColor = GymudSurface),
                modifier = Modifier.weight(1f).height(60.dp)
            ) {
                Text("Usuarios y Roles", color = GymudYellow)
            }
            Button(
                onClick = onNavigateToInventory,
                colors = ButtonDefaults.buttonColors(containerColor = GymudSurface),
                modifier = Modifier.weight(1f).height(60.dp)
            ) {
                Text("Inventario", color = GymudYellow)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Text("Usuarios Activos", color = Color.Gray, fontSize = 12.sp)
                Text("0", color = GymudYellow, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            }
            GlassCard(modifier = Modifier.weight(1f)) {
                Text("Ingresos Mensuales", color = Color.Gray, fontSize = 12.sp)
                Text("$0", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}