package com.example.gymud.screens.user

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.data.model.Measurement
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.ProgressViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProgressScreen(progressViewModel: ProgressViewModel) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val measurements by progressViewModel.measurements.collectAsState()
    val message by progressViewModel.message.collectAsState()
    val isLoading by progressViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var muscleMassInput by remember { mutableStateOf("") }
    var bodyFatInput by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    LaunchedEffect(currentUserId) {
        if (currentUserId.isNotEmpty()) {
            progressViewModel.loadUserMeasurements(currentUserId)
        }
    }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            progressViewModel.clearMessage()
            if (it.contains("éxito")) {
                showAddForm = false
                weightInput = ""
                heightInput = ""
                muscleMassInput = ""
                bodyFatInput = ""
            }
        }
    }

    val latestMeasurement = measurements.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenTitle("Progreso Corporal", "Tus métricas físicas y evolución")

        // Botón para desplegar/ocultar formulario de nuevo registro
        Button(
            onClick = { showAddForm = !showAddForm },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (showAddForm) GymudSurface else GymudYellow,
                contentColor = if (showAddForm) Color.White else Color.Black
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (showAddForm) "▲ Cancelar Registro" else "➕ Registrar Nuevas Medidas",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        // Formulario de ingreso de datos físicos
        if (showAddForm) {
            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                Column {
                    Text(
                        text = "Toma de Medidas Físicas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = weightInput,
                            onValueChange = { weightInput = it },
                            label = { Text("Peso (kg)", color = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = GymudYellow
                            )
                        )

                        OutlinedTextField(
                            value = heightInput,
                            onValueChange = { heightInput = it },
                            label = { Text("Altura (cm)", color = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = GymudYellow
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = muscleMassInput,
                            onValueChange = { muscleMassInput = it },
                            label = { Text("Masa Muscular (kg)", color = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = GymudYellow
                            )
                        )

                        OutlinedTextField(
                            value = bodyFatInput,
                            onValueChange = { bodyFatInput = it },
                            label = { Text("% Grasa Corporal", color = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = GymudYellow
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            progressViewModel.saveMeasurement(
                                userId = currentUserId,
                                weightStr = weightInput,
                                heightStr = heightInput,
                                muscleMassStr = muscleMassInput,
                                bodyFatStr = bodyFatInput
                            )
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black, strokeWidth = 2.dp)
                        } else {
                            Text("GUARDAR REGISTRO", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // Resumen de Métricas Recientes
        if (latestMeasurement != null) {
            Text(
                text = "Tus Métricas Recientes",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Tarjeta de IMC e IMM
            val (bmiCategory, bmiColor) = getBmiCategory(latestMeasurement.bmi)

            GlassCard(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Índice de Masa Corporal (IMC)", color = Color.Gray, fontSize = 13.sp)
                            Text("${latestMeasurement.bmi} - $bmiCategory", color = bmiColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Surface(color = bmiColor.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) {
                            Text("IMC", color = bmiColor, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Índice Masa Muscular (IMM)", color = Color.Gray, fontSize = 13.sp)
                            Text("${latestMeasurement.imm} %", color = GymudYellow, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            // Grid de Métricas Básicas
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                GlassCard(modifier = Modifier.weight(1f)) {
                    Text("Peso Corporal", color = Color.Gray, fontSize = 12.sp)
                    Text("${latestMeasurement.weight} kg", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                GlassCard(modifier = Modifier.weight(1f)) {
                    Text("Masa Muscular", color = Color.Gray, fontSize = 12.sp)
                    Text("${latestMeasurement.muscleMass} kg", color = GymudYellow, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                GlassCard(modifier = Modifier.weight(1f)) {
                    Text("% Grasa Corporal", color = Color.Gray, fontSize = 12.sp)
                    Text("${latestMeasurement.bodyFat} %", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                GlassCard(modifier = Modifier.weight(1f)) {
                    Text("Estatura", color = Color.Gray, fontSize = 12.sp)
                    Text("${latestMeasurement.height} cm", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Historial de Registros
        Text(
            text = "Historial de Progreso",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (measurements.isEmpty()) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text("No has registrado medidas aún. Presiona arriba para ingresar tu primera toma de datos.", color = Color.Gray)
            }
        } else {
            measurements.forEach { item ->
                MeasurementHistoryCard(item)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun MeasurementHistoryCard(item: Measurement) {
    val dateStr = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(item.date.toDate())

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(dateStr, color = GymudYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("IMC: ${item.bmi}", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Peso: ${item.weight} kg", color = Color.White, fontSize = 14.sp)
                Text("Masa M.: ${item.muscleMass} kg", color = Color.Gray, fontSize = 14.sp)
                Text("Grasa: ${item.bodyFat}%", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

private fun getBmiCategory(bmi: Double): Pair<String, Color> {
    return when {
        bmi < 18.5 -> Pair("Bajo Peso", Color(0xFF29B6F6))
        bmi in 18.5..24.9 -> Pair("Peso Normal", Color(0xFF66BB6A))
        bmi in 25.0..29.9 -> Pair("Sobrepeso", Color(0xFFFFA726))
        else -> Pair("Obesidad", Color(0xFFEF5350))
    }
}