package com.example.gymud.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.data.model.Inventory
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.AdminViewModel

import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun InventoryScreen(
    adminViewModel: AdminViewModel,
    onNavigateBack: () -> Unit = {}
) {
    val inventory by adminViewModel.inventory.collectAsState()
    val message by adminViewModel.message.collectAsState()
    val context = LocalContext.current

    var selectedFilterIndex by remember { mutableIntStateOf(1) } // 0: Todos, 1: Operativos, 2: Reportados

    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf("") }

    // Estado para el reporte de daño
    var reportingItem by remember { mutableStateOf<Inventory?>(null) }
    var reportDetailInput by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            adminViewModel.clearMessage()
        }
    }

    val operativeCount = inventory.count { !it.isDamaged }
    val reportedCount = inventory.count { it.isDamaged }

    val filteredInventory = when (selectedFilterIndex) {
        1 -> inventory.filter { !it.isDamaged }
        2 -> inventory.filter { it.isDamaged }
        else -> inventory
    }

    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        // Botón de Atrás y Título
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás",
                    tint = GymudYellow
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text("Gestión de Inventario", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text("Clasificación y reporte de mantenimiento", fontSize = 13.sp, color = Color.Gray)
            }
        }

        // Botón Agregar Nuevo Equipo
        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Nuevo Equipo", fontWeight = FontWeight.Bold)
        }

        // Pestañas / Chips de Filtrado y Clasificación
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filterLabels = listOf("Todos (${inventory.size})", "Operativos ($operativeCount)", "Reportados ($reportedCount)")
            
            filterLabels.forEachIndexed { index, label ->
                FilterChip(
                    selected = selectedFilterIndex == index,
                    onClick = { selectedFilterIndex = index },
                    label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (index) {
                            1 -> Color(0xFF2E7D32)
                            2 -> Color(0xFFC62828)
                            else -> GymudYellow
                        },
                        selectedLabelColor = if (index == 0) Color.Black else Color.White,
                        containerColor = GymudSurface,
                        labelColor = Color.Gray
                    )
                )
            }
        }

        if (filteredInventory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = when (selectedFilterIndex) {
                        1 -> "No hay equipos operativos registrados"
                        2 -> "¡Excelente! No hay equipos con reportes de falla"
                        else -> "No hay equipos en el inventario"
                    },
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredInventory, key = { it.id }) { item ->
                    InventoryCardItem(
                        item = item,
                        onReportClick = {
                            reportingItem = item
                            reportDetailInput = ""
                        },
                        onRepairClick = {
                            adminViewModel.reportInventoryDamage(item.id, isDamaged = false, reportDetail = "")
                        }
                    )
                }
            }
        }
    }

    // Diálogo para Agregar Nuevo Equipo
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = GymudSurface,
            title = { Text("Agregar Nuevo Equipo", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Nombre del equipo (ej: Prensa de Piernas)", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = GymudYellow
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newItemCategory,
                        onValueChange = { newItemCategory = it },
                        label = { Text("Categoría (Máquinas, Discos, Mancuernas)", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = GymudYellow
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        adminViewModel.addInventoryItem(newItemName, newItemCategory)
                        newItemName = ""
                        newItemCategory = ""
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black)
                ) {
                    Text("Guardar Equipo", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }

    // Diálogo para Redactar el Reporte de Falla a Detalle
    if (reportingItem != null) {
        AlertDialog(
            onDismissRequest = { reportingItem = null },
            containerColor = GymudSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF5350))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reportar Falla en ${reportingItem?.name}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Escribe a detalle el motivo del reporte o mantenimiento requerido:",
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = reportDetailInput,
                        onValueChange = { reportDetailInput = it },
                        placeholder = { Text("Ej: Cable deshilachado, pantalla no enciende...", color = Color.DarkGray) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedIndicatorColor = GymudYellow
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        reportingItem?.let { item ->
                            adminViewModel.reportInventoryDamage(
                                itemId = item.id,
                                isDamaged = true,
                                reportDetail = reportDetailInput.ifBlank { "Reportado por administración" }
                            )
                        }
                        reportingItem = null
                        reportDetailInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828), contentColor = Color.White)
                ) {
                    Text("Guardar y Enviar Reporte", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { reportingItem = null }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun InventoryCardItem(
    item: Inventory,
    onReportClick: () -> Unit,
    onRepairClick: () -> Unit
) {
    val cardBg = if (item.isDamaged) Color(0xFF3B1818) else Color.White.copy(alpha = 0.05f)

    GlassCard(modifier = Modifier.fillMaxWidth(), backgroundColor = cardBg) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Categoría: ${item.category.ifEmpty { "General" }}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (!item.isDamaged) Icons.Default.CheckCircle else Icons.Default.Build,
                            contentDescription = null,
                            tint = if (!item.isDamaged) Color(0xFF66BB6A) else Color(0xFFEF5350),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (!item.isDamaged) "OPERATIVO" else "EN MANTENIMIENTO",
                            color = if (!item.isDamaged) Color(0xFF66BB6A) else Color(0xFFEF5350),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Botón según el estado
                if (item.isDamaged) {
                    Button(
                        onClick = onRepairClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("✅ Marcar Reparado", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = onReportClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF332200),
                            contentColor = GymudYellow
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("⚠️ Reportar Daño", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Si está reportado, mostrar inmediatamente debajo la nota explicativa
            if (item.isDamaged) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFF3E1E1E),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "📋 Detalle del Reporte:",
                            color = Color(0xFFFF8A80),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.reportDetail.ifBlank { "Sin descripción ingresada" },
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}