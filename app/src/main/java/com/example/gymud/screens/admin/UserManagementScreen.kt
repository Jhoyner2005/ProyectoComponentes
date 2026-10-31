package com.example.gymud.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gymud.screens.common.*

@Composable
fun UserManagementScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        ScreenTitle("Miembros")

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar usuario...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, tint = GymudYellow, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = GymudSurface,
                focusedContainerColor = GymudSurface,
                focusedIndicatorColor = GymudYellow
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Usuario #$it", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Membresía Activa", color = Color.Green)
                        }
                        Row {
                            TextButton(onClick = {}) { Text("Editar", color = GymudYellow) }
                            TextButton(onClick = {}) { Text("Bloquear", color = Color.Red) }
                        }
                    }
                }
            }
        }
    }
}