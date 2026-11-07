package com.example.gymud.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.AdminViewModel

@Composable
fun AdminDashboardScreen(
    onNavigateToUsers: () -> Unit,
    onNavigateToInventory: () -> Unit,
    adminViewModel: AdminViewModel
) {
    val activeUsersCount by adminViewModel.activeUsersCount.collectAsState()
    val totalIncome by adminViewModel.totalMonthlyIncome.collectAsState()
    val usersList by adminViewModel.users.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenTitle("Panel de Control", "Vista General del Gimnasio")

        // Botones de Navegación del Admin
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onNavigateToUsers,
                colors = ButtonDefaults.buttonColors(containerColor = GymudSurface),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                Text("Usuarios y Roles", color = GymudYellow, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onNavigateToInventory,
                colors = ButtonDefaults.buttonColors(containerColor = GymudSurface),
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
            ) {
                Text("Inventario", color = GymudYellow, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjetas de Métricas en vivo
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Text("Usuarios Activos", color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = "$activeUsersCount",
                    color = GymudYellow,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            GlassCard(modifier = Modifier.weight(1f)) {
                Text("Ingresos Mensuales", color = Color.Gray, fontSize = 12.sp)
                Text(
                    text = "$${totalIncome.toInt()}",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Resumen Estadístico General
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Resumen General",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Usuarios Registrados:", color = Color.Gray)
                Text("${usersList.size}", color = GymudYellow, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Entrenadores Activos:", color = Color.Gray)
                Text(
                    text = "${usersList.count { it.role == "TRAINER" }}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Administradores:", color = Color.Gray)
                Text(
                    text = "${usersList.count { it.role == "ADMIN" }}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}