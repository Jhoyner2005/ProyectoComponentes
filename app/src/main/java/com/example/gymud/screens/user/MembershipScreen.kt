package com.example.gymud.screens.user

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.data.model.MembershipPaymentHistory
import com.example.gymud.data.model.MembershipPlan
import com.example.gymud.screens.common.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MembershipScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Planes a escoger, 1 = Historial de pagos
    var planToPurchase by remember { mutableStateOf<MembershipPlan?>(null) }
    var selectedPaymentMethod by remember { mutableStateOf("Nequi / Daviplata") }

    // Membresía activa del usuario
    var activePlanName by remember { mutableStateOf("Plan Anual VIP") }
    var activePlanExpiry by remember { mutableStateOf("15 de Noviembre, 2026") }

    // Planes disponibles
    val availablePlans = remember {
        listOf(
            MembershipPlan(
                id = "plan_daily",
                name = "Pase Diario",
                priceDisplay = "$20.000 COP",
                priceAmount = 20000.0,
                durationText = "Por 1 Día",
                description = "Ideal para entrenamientos ocasionales o visitantes.",
                features = listOf(
                    "Acceso completo a zona de pesas y maquinas",
                    "Uso de vestidores y duchas",
                    "Válido por 24 horas"
                )
            ),
            MembershipPlan(
                id = "plan_monthly",
                name = "Plan Mensual Basico",
                priceDisplay = "$90.000 COP",
                priceAmount = 90000.0,
                durationText = "Por 1 Mes",
                description = "Tu pase mensual para mantener la constancia fitness.",
                features = listOf(
                    "Acceso libre a zona de pesas y cardio",
                    "Uso de vestidores y lockers diarios",
                    "App GYMUD basica para registro de rutinas"
                )
            ),
            MembershipPlan(
                id = "plan_quarterly",
                name = "Plan Trimestral Pro",
                priceDisplay = "$240.000 COP",
                priceAmount = 240000.0,
                durationText = "Por 3 Meses",
                description = "Perfecto para obtener resultados visibles a mediano plazo.",
                features = listOf(
                    "Acceso ilimitado a todas las sedes GYMUD",
                    "Clases grupales (Spinning, Yoga, Zumba)",
                    "1 Evaluacion corporal InBody por trimestre",
                    "Descuento del 10% en suplementos"
                ),
                isPopular = true
            ),
            MembershipPlan(
                id = "plan_semiannual",
                name = "Plan Semestral Fit",
                priceDisplay = "$450.000 COP",
                priceAmount = 450000.0,
                durationText = "Por 6 Meses",
                description = "Equilibrio ideal entre excelente precio y compromiso continuo.",
                features = listOf(
                    "Acceso ilimitado a zonas de pesas, cardio y funcionales",
                    "Clases grupales (Spinning, Yoga, Zumba, HIIT)",
                    "2 Evaluaciones corporales InBody completas",
                    "Descuento del 15% en tienda de suplementacion",
                    "1 Pase de invitado gratis por mes"
                )
            ),
            MembershipPlan(
                id = "plan_annual",
                name = "Plan Anual VIP",
                priceDisplay = "$790.000 COP",
                priceAmount = 790000.0,
                durationText = "Por 12 Meses",
                description = "El plan definitivo para transformar tu estilo de vida.",
                features = listOf(
                    "Acceso total ilimitado 24/7",
                    "Zona VIP: Sauna, Turco y Jacuzzi",
                    "Seguimiento nutricional y con entrenador",
                    "1 Pase gratis de invitado por mes",
                    "Locker personal asignado",
                    "Descuento del 20% en tienda de suplementos"
                ),
                isBestValue = true
            )
        )
    }

    // Historial de pagos realizados
    val paymentHistoryList = remember {
        mutableStateListOf(
            MembershipPaymentHistory(
                id = "pay_001",
                planName = "Plan Anual VIP",
                amountDisplay = "$790.000 COP",
                paymentDate = "15 Nov 2025",
                startDate = "15 Nov 2025",
                endDate = "15 Nov 2026",
                paymentMethod = "Tarjeta de Credito **** 4521",
                status = "ACTIVA"
            ),
            MembershipPaymentHistory(
                id = "pay_002",
                planName = "Plan Trimestral Pro",
                amountDisplay = "$240.000 COP",
                paymentDate = "15 Ago 2025",
                startDate = "15 Ago 2025",
                endDate = "15 Nov 2025",
                paymentMethod = "Nequi / N° Ref 98412",
                status = "COMPLETADA"
            ),
            MembershipPaymentHistory(
                id = "pay_003",
                planName = "Plan Mensual Basico",
                amountDisplay = "$90.000 COP",
                paymentDate = "15 Jul 2025",
                startDate = "15 Jul 2025",
                endDate = "15 Ago 2025",
                paymentMethod = "PSE / Bancolombia",
                status = "COMPLETADA"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        ScreenTitle("Mi Membresía", "Gestion de suscripciones y pagos")

        // Membresía Activa actual
        ActiveMembershipCard(
            planName = activePlanName,
            expiryDate = activePlanExpiry,
            onRenewClick = { selectedTab = 0 }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Pestañas (Planes a escoger / Historial de Pagos)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = GymudSurface,
            contentColor = GymudYellow,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Planes a Escoger",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                },
                selectedContentColor = GymudYellow,
                unselectedContentColor = Color.Gray
            )

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Historial (${paymentHistoryList.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                },
                selectedContentColor = GymudYellow,
                unselectedContentColor = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // pestaña seleccionada
        if (selectedTab == 0) {
            // LISTA DE PLANES A ESCOGER
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Selecciona el plan ideal para ti",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                availablePlans.forEach { plan ->
                    PlanItemCard(
                        plan = plan,
                        onSelectPlan = { planToPurchase = plan }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        } else {
            // HISTORIAL DE PAGOS
            if (paymentHistoryList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay historial de pagos registrado.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(paymentHistoryList, key = { it.id }) { historyItem ->
                        PaymentHistoryCard(history = historyItem)
                    }
                }
            }
        }
    }

    // Modal / Confirmación de Pago de Membresía
    planToPurchase?.let { plan ->
        AlertDialog(
            onDismissRequest = { planToPurchase = null },
            containerColor = GymudSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GymudYellow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Confirmar Adquisicion",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Estas a punto de suscribirte a:",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = plan.name,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${plan.priceDisplay} (${plan.durationText})",
                        color = GymudYellow,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Metodo de Pago:",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val paymentOptions = listOf("Nequi / Daviplata", "Tarjeta de Credito / Debito", "PSE / Transferencia", "Pago Presencial en Caja")
                    paymentOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedPaymentMethod = option }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedPaymentMethod == option,
                                onClick = { selectedPaymentMethod = option },
                                colors = RadioButtonDefaults.colors(selectedColor = GymudYellow)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val currentDate = Date()
                        val calendar = Calendar.getInstance()
                        calendar.time = currentDate
                        when (plan.id) {
                            "plan_daily" -> calendar.add(Calendar.DAY_OF_YEAR, 1)
                            "plan_monthly" -> calendar.add(Calendar.MONTH, 1)
                            "plan_quarterly" -> calendar.add(Calendar.MONTH, 3)
                            "plan_semiannual" -> calendar.add(Calendar.MONTH, 6)
                            else -> calendar.add(Calendar.YEAR, 1)
                        }
                        val endDateStr = dateFormat.format(calendar.time)
                        val startDateStr = dateFormat.format(currentDate)

                        // Actualizar plan activo
                        activePlanName = plan.name
                        activePlanExpiry = endDateStr

                        // Agregar al historial
                        val newHistory = MembershipPaymentHistory(
                            id = "pay_${System.currentTimeMillis()}",
                            planName = plan.name,
                            amountDisplay = plan.priceDisplay,
                            paymentDate = startDateStr,
                            startDate = startDateStr,
                            endDate = endDateStr,
                            paymentMethod = selectedPaymentMethod,
                            status = "ACTIVA"
                        )
                        paymentHistoryList.add(0, newHistory)

                        Toast.makeText(context, "¡Membresía ${plan.name} activada con exito!", Toast.LENGTH_LONG).show()
                        planToPurchase = null
                        selectedTab = 1
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black)
                ) {
                    Text("Pagar y Activar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { planToPurchase = null }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ActiveMembershipCard(
    planName: String,
    expiryDate: String,
    onRenewClick: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(GymudYellow.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = GymudYellow,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "TU MEMBRESIA ACTUAL",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = planName,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                color = Color(0xFF2E7D32).copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF4CAF50))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ACTIVA",
                        color = Color(0xFF4CAF50),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Vence el: $expiryDate",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }

            TextButton(
                onClick = onRenewClick,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Renovar Plan",
                    color = GymudYellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PlanItemCard(
    plan: MembershipPlan,
    onSelectPlan: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (plan.isPopular || plan.isBestValue) 1.5.dp else 1.dp,
                color = if (plan.isBestValue) GymudYellow else if (plan.isPopular) Color(0xFF2196F3) else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column {
            // Popular / Mejor Valor
            if (plan.isPopular || plan.isBestValue) {
                Surface(
                    color = if (plan.isBestValue) GymudYellow else Color(0xFF2196F3),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (plan.isBestValue) "MEJOR OPCIÓN" else "MÁS POPULAR",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Nombre y Descripcion
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = plan.name,
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = plan.description,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Precio y Duración
            Surface(
                color = Color.White.copy(alpha = 0.07f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = plan.priceDisplay,
                        color = GymudYellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Surface(
                        color = GymudYellow.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = plan.durationText,
                            color = GymudYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))

            // Lista de Beneficios
            plan.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = GymudYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feature,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSelectPlan,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (plan.isBestValue || plan.isPopular) GymudYellow else Color.White.copy(alpha = 0.15f),
                    contentColor = if (plan.isBestValue || plan.isPopular) Color.Black else Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Escoger Plan ${plan.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun PaymentHistoryCard(history: MembershipPaymentHistory) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = GymudYellow,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = history.planName,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pagado: ${history.paymentDate}",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = history.amountDisplay,
                    color = GymudYellow,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = if (history.status == "ACTIVA") Color(0xFF2E7D32).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = history.status,
                        color = if (history.status == "ACTIVA") Color(0xFF4CAF50) else Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Vigencia: ${history.startDate} - ${history.endDate}",
                color = Color.LightGray,
                fontSize = 11.sp
            )
            Text(
                text = history.paymentMethod,
                color = Color.Gray,
                fontSize = 11.sp
            )
        }
    }
}
