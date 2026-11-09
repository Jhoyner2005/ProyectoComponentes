
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

@Composable
fun ProgressScreen(
    userId: String,
    progressViewModel: ProgressViewModel
) {
    val measurements by progressViewModel.measurements.collectAsState()
    val message by progressViewModel.message.collectAsState()
    val isLoading by progressViewModel.isLoading.collectAsState()

    val context = LocalContext.current

    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var muscleMassInput by remember { mutableStateOf("") }
    var bodyFatInput by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            progressViewModel.loadUserMeasurements(userId)
        }
    }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            progressViewModel.clearMessage()

            if (it.contains("success", ignoreCase = true) ||
                it.contains("éxito", ignoreCase = true)
            ) {
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
            title = "Physical Progress",
            subtitle = "Your physical measurements and progress"
        )

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
                    "CANCEL"
                } else {
                    "+ ADD NEW MEASUREMENT"
                },
                fontWeight = FontWeight.Bold
            )
        }

        if (showAddForm) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "New Physical Measurement",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    MeasurementInput(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = "Weight (kg)"
                    )

                    MeasurementInput(
                        value = heightInput,
                        onValueChange = { heightInput = it },
                        label = "Height (cm)"
                    )

                    MeasurementInput(
                        value = muscleMassInput,
                        onValueChange = { muscleMassInput = it },
                        label = "Muscle Mass (kg)"
                    )

                    MeasurementInput(
                        value = bodyFatInput,
                        onValueChange = { bodyFatInput = it },
                        label = "Body Fat (%)"
                    )

                    Button(
                        onClick = {
                            progressViewModel.saveMeasurement(
                                userId = userId,
                                weightStr = weightInput,
                                heightStr = heightInput,
                                muscleMassStr = muscleMassInput,
                                bodyFatStr = bodyFatInput
                            )
                        },
                        enabled = !isLoading && userId.isNotBlank(),
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
                                text = "SAVE MEASUREMENT",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "Physical Summary",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        if (latestMeasurement != null) {
            val bmiCategory = getBmiCategory(latestMeasurement.bmi)

            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Body Mass Index",
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
                        label = "Muscle Mass Index",
                        value = "${latestMeasurement.imm} %"
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Weight",
                    value = "${latestMeasurement.weight} kg",
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Muscle Mass",
                    value = "${latestMeasurement.muscleMass} kg",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MetricCard(
                    title = "Body Fat",
                    value = "${latestMeasurement.bodyFat} %",
                    modifier = Modifier.weight(1f)
                )

                MetricCard(
                    title = "Height",
                    value = "${latestMeasurement.height} cm",
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "You do not have any registered measurements yet.",
                    color = Color.Gray
                )
            }
        }

        Text(
            text = "Physical Evolution",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        ProgressChart(
            title = "Weight Evolution",
            measurements = measurements,
            valueSelector = { it.weight },
            unit = "kg",
            lineColor = GymudYellow
        )

        ProgressChart(
            title = "Muscle Mass Evolution",
            measurements = measurements,
            valueSelector = { it.muscleMass },
            unit = "kg",
            lineColor = Color(0xFF4FC3F7)
        )

        Text(
            text = "Measurement History",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        if (measurements.isEmpty()) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "No records available.",
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
                    text = "There is not enough data to display the chart.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            } else {
                val minimum = values.minOrNull() ?: 0.0
                val maximum = values.maxOrNull() ?: 0.0

                Text(
                    text = "Latest record: ${values.last()} $unit",
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

                    val points = values.mapIndexed { index, value ->
                        val x = if (values.size == 1) {
                            chartWidth / 2
                        } else {
                            chartWidth * index / (values.size - 1)
                        }

                        val normalizedValue = (value - minimum) / range

                        val y = chartHeight - verticalPadding -
                                (normalizedValue * (chartHeight - verticalPadding * 2)).toFloat()

                        Offset(x, y)
                    }

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Oldest",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "Newest",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

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
                    text = "BMI: ${item.bmi}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))

            MetricRow(
                label = "Body Weight",
                value = "${item.weight} kg"
            )

            MetricRow(
                label = "Muscle Mass",
                value = "${item.muscleMass} kg"
            )

            MetricRow(
                label = "Body Fat",
                value = "${item.bodyFat} %"
            )

            MetricRow(
                label = "Height",
                value = "${item.height} cm"
            )
        }
    }
}

private fun getBmiCategory(bmi: Double): Pair<String, Color> {
    return when {
        bmi < 18.5 -> Pair("Underweight", Color(0xFF29B6F6))
        bmi < 25.0 -> Pair("Normal Weight", Color(0xFF66BB6A))
        bmi < 30.0 -> Pair("Overweight", Color(0xFFFFA726))
        else -> Pair("Obesity", Color(0xFFEF5350))
    }
}
