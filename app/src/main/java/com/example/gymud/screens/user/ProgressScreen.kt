
package com.example.gymud.screens.user

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.data.model.Measurement
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.ProgressViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private const val DEMO_USER_ID = "demo_user"

@Composable
fun ProgressScreen(progressViewModel: ProgressViewModel) {

    val measurements by progressViewModel.measurements.collectAsState()
    val message by progressViewModel.message.collectAsState()
    val isLoading by progressViewModel.isLoading.collectAsState()

    val context = LocalContext.current

    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var muscleMassInput by remember { mutableStateOf("") }
    var bodyFatInput by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        progressViewModel.loadUserMeasurements(DEMO_USER_ID)
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
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ScreenTitle(
            title = "Progreso Corporal",
            subtitle = "Tus métricas físicas y evolución"
        )

        // Button to show the measurement form
        Button(
            onClick = { showAddForm = !showAddForm },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (showAddForm) GymudSurface else GymudYellow,
                contentColor = if (showAddForm) Color.White else Color.Black
            )
        ) {
            Text(
                text = if (showAddForm) {
                    "CANCELAR REGISTRO"
                } else {
                    "+ REGISTRAR NUEVAS MEDIDAS"
                },
                fontWeight = FontWeight.Bold
            )
        }

        // Measurement registration form
        if (showAddForm) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text(
                        text = "Nueva medición física",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    MeasurementInput(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = "Peso (kg)"
                    )

                    MeasurementInput(
                        value = heightInput,
                        onValueChange = { heightInput = it },
                        label = "Estatura (cm)"
                    )

                    MeasurementInput(
                        value = muscleMassInput,
                        onValueChange = { muscleMassInput = it },
                        label = "Masa muscular (kg)"
                    )

                    MeasurementInput(
                        value = bodyFatInput,
                        onValueChange = { bodyFatInput = it },
                        label = "Grasa corporal (%)"
                    )

                    Button(
                        onClick = {
                            progressViewModel.saveMeasurement(
                                userId = DEMO_USER_ID,
                                weightStr = weightInput,
                                heightStr = heightInput,
                                muscleMassStr = muscleMassInput,
                                bodyFatStr = bodyFatInput
                            )
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GymudYellow,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.Black
                            )
                        } else {
                            Text(
                                text = "GUARDAR MEDICIÓN",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Latest physical measurements
        Text(
            text = "Resumen de tu estado físico",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        if (latestMeasurement != null) {

            val bmiCategory = getBmiCategory(latestMeasurement.bmi)

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    Text(
                        text = "Índice de Masa Corporal",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${latestMeasurement.bmi}",
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Surface(
                            color = bmiCategory.second.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = bmiCategory.first,
                                color = bmiCategory.second,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

                    MetricRow(
                        label = "Índice de masa muscular",
                        value = "${latestMeasurement.imm} %"
                    )
                }
            }

            // Basic physical metrics
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Peso",
                    value = "${latestMeasurement.weight} kg",
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Masa muscular",
                    value = "${latestMeasurement.muscleMass} kg",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Grasa corporal",
                    value = "${latestMeasurement.bodyFat} %",
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Estatura",
                    value = "${latestMeasurement.height} cm",
                    modifier = Modifier.weight(1f)
                )
            }

        } else {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Todavía no tienes mediciones registradas.",
                    color = Color.Gray
                )
            }
        }

        // Weight evolution chart
        Text(
            text = "Evolución física",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        ProgressChart(
            title = "Evolución del peso",
            measurements = measurements,
            valueSelector = { it.weight },
            unit = "kg",
            lineColor = GymudYellow
        )

        // Muscle mass evolution chart
        ProgressChart(
            title = "Evolución de masa muscular",
            measurements = measurements,
            valueSelector = { it.muscleMass },
            unit = "kg",
            lineColor = Color(0xFF4FC3F7)
        )

        // Measurement history
        Text(
            text = "Historial de mediciones",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        if (measurements.isEmpty()) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "No hay registros disponibles.",
                    color = Color.Gray
                )
            }
        } else {
            measurements.forEach { measurement ->
                MeasurementHistoryCard(measurement)
            }
        }
    }
}

// Reusable input for physical measurements
@Composable
private fun MeasurementInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, color = Color.Gray)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = GymudYellow,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = GymudYellow,
            cursorColor = GymudYellow
        )
    )
}

// Reusable card for a physical metric
@Composable
private fun MetricCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column {
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = GymudYellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Display a label and its measurement
@Composable
private fun MetricRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 13.sp
        )

        Text(
            text = value,
            color = GymudYellow,
            fontWeight = FontWeight.Bold
        )
    }
}

// Chart to display physical progress
@Composable
private fun ProgressChart(
    title: String,
    measurements: List<Measurement>,
    valueSelector: (Measurement) -> Double,
    unit: String,
    lineColor: Color
) {
    val chartMeasurements = measurements.reversed()
    val values = chartMeasurements.map { valueSelector(it) }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            if (values.isEmpty()) {
                Text(
                    text = "No hay datos suficientes para mostrar la gráfica.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            } else {

                val minimum = values.minOrNull() ?: 0.0
                val maximum = values.maxOrNull() ?: 0.0

                Text(
                    text = "Último registro: ${values.last()} $unit",
                    color = lineColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    val chartWidth = size.width
                    val chartHeight = size.height
                    val verticalPadding = 15f

                    val range = if (maximum == minimum) {
                        1.0
                    } else {
                        maximum - minimum
                    }

                    // Draw horizontal guide lines
                    for (index in 0..3) {
                        val y = verticalPadding +
                                (chartHeight - verticalPadding * 2) * index / 3

                        drawLine(
                            color = Color.White.copy(alpha = 0.15f),
                            start = Offset(0f, y),
                            end = Offset(chartWidth, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    if (values.isNotEmpty()) {

                        val points = values.mapIndexed { index, value ->

                            val x = if (values.size == 1) {
                                chartWidth / 2
                            } else {
                                chartWidth * index / (values.size - 1)
                            }

                            val normalizedValue =
                                (value - minimum) / range

                            val y = chartHeight - verticalPadding -
                                    (normalizedValue * (chartHeight - verticalPadding * 2)).toFloat()

                            Offset(x, y)
                        }

                        // Connect measurement points
                        if (points.size > 1) {
                            val path = Path().apply {
                                moveTo(points.first().x, points.first().y)

                                points.drop(1).forEach { point ->
                                    lineTo(point.x, point.y)
                                }
                            }

                            drawPath(
                                path = path,
                                color = lineColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                        }

                        // Draw measurement points
                        points.forEach { point ->
                            drawCircle(
                                color = lineColor,
                                radius = 5.dp.toPx(),
                                center = point
                            )

                            drawCircle(
                                color = Color.White,
                                radius = 2.dp.toPx(),
                                center = point
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Más antiguo",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "Más reciente",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

// Display one historical measurement
@Composable
fun MeasurementHistoryCard(item: Measurement) {

    val dateFormat = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    )

    val dateText = dateFormat.format(item.date.toDate())

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateText,
                    color = GymudYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Text(
                    text = "IMC: ${item.bmi}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

            MetricRow(
                label = "Peso corporal",
                value = "${item.weight} kg"
            )

            MetricRow(
                label = "Masa muscular",
                value = "${item.muscleMass} kg"
            )

            MetricRow(
                label = "Grasa corporal",
                value = "${item.bodyFat} %"
            )

            MetricRow(
                label = "Estatura",
                value = "${item.height} cm"
            )
        }
    }
}

// Classify the body mass index
private fun getBmiCategory(bmi: Double): Pair<String, Color> {
    return when {
        bmi < 18.5 -> Pair("Bajo peso", Color(0xFF29B6F6))
        bmi < 25.0 -> Pair("Peso normal", Color(0xFF66BB6A))
        bmi < 30.0 -> Pair("Sobrepeso", Color(0xFFFFA726))
        else -> Pair("Obesidad", Color(0xFFEF5350))
    }
}
