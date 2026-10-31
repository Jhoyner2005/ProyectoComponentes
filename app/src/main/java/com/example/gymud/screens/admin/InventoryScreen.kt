package com.example.gymud.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymud.screens.common.*

@Composable
fun InventoryScreen() {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp).verticalScroll(rememberScrollState())) {
        ScreenTitle("Equipamiento", "Gestión y mantenimiento")

        val equipments = listOf(
            Triple("Smith Machine 2", true, "OK"),
            Triple("Treadmill K", false, "Mantenimiento"),
            Triple("Bench Press", false, "Mantenimiento")
        )

        equipments.forEach { (name, isOk, status) ->
            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isOk) Icons.Default.CheckCircle else Icons.Default.Build,
                                contentDescription = null,
                                tint = if (isOk) Color.Green else Color.Red,
                                modifier = Modifier.size(16.dp).padding(end = 4.dp)
                            )
                            Text(status, color = if (isOk) Color.Green else Color.Red)
                        }
                    }
                    if (!isOk) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = GymudSurface, contentColor = GymudYellow)
                        ) {
                            Text("Reparar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black)
        ) {
            Text("Programar Mantenimiento General", fontWeight = FontWeight.Bold)
        }
    }
}