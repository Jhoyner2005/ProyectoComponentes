package com.example.gymud.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
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
fun MembershipScreen() {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        ScreenTitle("Mi Membresía")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.CreditCard, contentDescription = null, tint = GymudYellow, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("PLAN: VIP Activo", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Vence: 15 de Noviembre, 2026", color = Color.Gray)
            Text("Estado: Pagado", color = Color.Green)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Renovar Membresía", fontWeight = FontWeight.Bold)
            }
        }
    }
}