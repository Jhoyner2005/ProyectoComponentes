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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.AdminViewModel

@Composable
fun InventoryScreen(adminViewModel: AdminViewModel) {
    val inventory by adminViewModel.inventory.collectAsState()
    val message by adminViewModel.message.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemCategory by remember { mutableStateOf("") }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            adminViewModel.clearMessage()
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScreenTitle("Equipamiento", "Inventario y mantenimiento")
        }

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GymudYellow, contentColor = Color.Black),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agregar Nuevo Equipo", fontWeight = FontWeight.Bold)
        }

        if (inventory.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay equipos registrados en el inventario", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(inventory, key = { it.id }) { item ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
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
                                        tint = if (!item.isDamaged) Color.Green else Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (!item.isDamaged) "Operativo" else "En Mantenimiento",
                                        color = if (!item.isDamaged) Color.Green else Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { adminViewModel.toggleInventoryStatus(item.id, item.isDamaged) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (item.isDamaged) Color(0xFF2E7D32) else GymudSurface,
                                    contentColor = if (item.isDamaged) Color.White else GymudYellow
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (item.isDamaged) "Reparar" else "Reportar Daño",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para agregar equipo
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = GymudSurface,
            title = { Text("Agregar Equipo", color = Color.White, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Nombre del equipo", color = Color.Gray) },
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
                        label = { Text("Categoría (Máquinas, Discos, etc.)", color = Color.Gray) },
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
                    Text("Guardar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}