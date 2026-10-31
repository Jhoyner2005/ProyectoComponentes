package com.example.gymud.screens.trainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gymud.screens.common.*

@Composable
fun ChatScreen() {
    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        ScreenTitle("Mensajes")
        GlassCard(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Text("No hay mensajes aún.", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = "", onValueChange = {},
            placeholder = { Text("Escribe un mensaje...", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(unfocusedContainerColor = GymudSurface, focusedContainerColor = GymudSurface, focusedTextColor = Color.White)
        )
    }
}