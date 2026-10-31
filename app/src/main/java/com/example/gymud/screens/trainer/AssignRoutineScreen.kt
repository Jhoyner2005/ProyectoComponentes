package com.example.gymud.screens.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gymud.screens.common.*

@Composable
fun AssignRoutineScreen() {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        ScreenTitle("Crear Rutina", "Asignar a un cliente")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Text("Seleccionar Cliente (Vacío)", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Agregar Ejercicios (Vacío)", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black)) {
                Text("Guardar Rutina")
            }
        }
    }
}