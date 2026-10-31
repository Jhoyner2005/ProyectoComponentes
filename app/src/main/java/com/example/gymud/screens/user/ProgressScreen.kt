package com.example.gymud.screens.user

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
import com.example.gymud.screens.common.*

@Composable
fun ProgressScreen() {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp).verticalScroll(rememberScrollState())) {
        ScreenTitle("Progreso Corporal", "Tus métricas recientes")

        val metrics = listOf(
            Pair("Peso Corporal (kg)", "72.5 kg"),
            Pair("Porcentaje de Grasa (%)", "15.2 %"),
            Pair("Masa Muscular (kg)", "35.8 kg")
        )

        metrics.forEach { metric ->
            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(metric.first, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(metric.second, color = GymudYellow, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Placeholder para la gráfica
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).background(Color.White.copy(alpha = 0.02f))) {
                    Text("Gráfica de tendencia...", color = Color.DarkGray, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}