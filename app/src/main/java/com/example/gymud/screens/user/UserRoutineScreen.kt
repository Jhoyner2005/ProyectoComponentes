package com.example.gymud.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*

@Composable
fun UserRoutineScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        ScreenTitle("Tus Rutinas", "Entrenamiento del día")

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar ejercicios...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, tint = GymudYellow, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = GymudSurface,
                focusedContainerColor = GymudSurface,
                focusedIndicatorColor = GymudYellow,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(6) { index ->
                GlassCard {
                    // Placeholder visual para el ejercicio
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(Color.White.copy(alpha = 0.1f)).padding(bottom = 8.dp))
                    Text("Ejercicio ${index + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Series: 3", color = Color.Gray, fontSize = 12.sp)
                    Text("Reps: 10-12", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}