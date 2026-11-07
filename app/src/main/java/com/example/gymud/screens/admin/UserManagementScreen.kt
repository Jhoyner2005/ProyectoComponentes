package com.example.gymud.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.data.model.User
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.AdminViewModel

@Composable
fun UserManagementScreen(adminViewModel: AdminViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterIndex by remember { mutableIntStateOf(0) }

    val users by adminViewModel.users.collectAsState()
    val message by adminViewModel.message.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            adminViewModel.clearMessage()
        }
    }

    // Conteos para los filtros
    val totalCount = users.size
    val clientsCount = users.count { it.role == "USER" }
    val activeClientsCount = users.count { it.role == "USER" && it.isMembershipActive }
    val inactiveClientsCount = users.count { it.role == "USER" && !it.isMembershipActive }
    val trainersCount = users.count { it.role == "TRAINER" }
    val adminsCount = users.count { it.role == "ADMIN" }

    val filteredUsers = users.filter { user ->
        val matchesSearch = user.name.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilterIndex) {
            1 -> user.role == "USER"
            2 -> user.role == "USER" && user.isMembershipActive
            3 -> user.role == "USER" && !user.isMembershipActive
            4 -> user.role == "TRAINER"
            5 -> user.role == "ADMIN"
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        ScreenTitle("Gestión de Miembros", "Administrar roles y estado de membresías")

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar por nombre o correo...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, tint = GymudYellow, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = GymudSurface,
                focusedContainerColor = GymudSurface,
                focusedIndicatorColor = GymudYellow,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        // Chips de Clasificación y Filtrado por Rol y Membresía
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                "Todos ($totalCount)",
                "Clientes ($clientsCount)",
                "Activos ($activeClientsCount)",
                "Inactivos ($inactiveClientsCount)",
                "Entrenadores ($trainersCount)",
                "Admins ($adminsCount)"
            )

            items(filters.size) { index ->
                FilterChip(
                    selected = selectedFilterIndex == index,
                    onClick = { selectedFilterIndex = index },
                    label = { Text(filters[index], fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (index) {
                            2 -> Color(0xFF2E7D32) // Verde Activos
                            3 -> Color(0xFFC62828) // Rojo Inactivos
                            4 -> Color(0xFF1E88E5) // Azul Entrenadores
                            5 -> Color(0xFFE53935) // Rojo Admins
                            else -> GymudYellow
                        },
                        selectedLabelColor = if (index == 0 || index == 1) Color.Black else Color.White,
                        containerColor = GymudSurface,
                        labelColor = Color.Gray
                    )
                )
            }
        }

        if (filteredUsers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontraron usuarios en esta categoría", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredUsers, key = { it.uid }) { user ->
                    UserCardItem(
                        user = user,
                        onRoleChange = { newRole -> adminViewModel.updateUserRoleDirect(user.uid, newRole) },
                        onToggleMembership = { currentStatus -> adminViewModel.toggleUserMembership(user.uid, currentStatus) }
                    )
                }
            }
        }
    }
}

@Composable
fun UserCardItem(
    user: User,
    onRoleChange: (String) -> Unit,
    onToggleMembership: (Boolean) -> Unit
) {
    var expandedRoleMenu by remember { mutableStateOf(false) }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name.ifEmpty { "Sin nombre" },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = user.email,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                // Menú desplegable para cambiar Rol
                Box {
                    Button(
                        onClick = { expandedRoleMenu = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when (user.role) {
                                "ADMIN" -> Color(0xFFE53935)
                                "TRAINER" -> Color(0xFF1E88E5)
                                else -> GymudSurface
                            }
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Rol: ${user.role}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expandedRoleMenu,
                        onDismissRequest = { expandedRoleMenu = false },
                        modifier = Modifier.background(GymudSurface)
                    ) {
                        listOf("USER", "TRAINER", "ADMIN").forEach { roleOption ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = roleOption,
                                        color = if (user.role == roleOption) GymudYellow else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    onRoleChange(roleOption)
                                    expandedRoleMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // La membresía SOLO se muestra si el usuario es un cliente (USER)
            if (user.role == "USER") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Estado de Membresía
                    Text(
                        text = if (user.isMembershipActive) "Membresía: ACTIVA" else "Membresía: INACTIVA",
                        color = if (user.isMembershipActive) Color(0xFF66BB6A) else Color(0xFFEF5350),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    // Botón Activar / Desactivar
                    Button(
                        onClick = { onToggleMembership(user.isMembershipActive) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (user.isMembershipActive) Color.DarkGray else Color(0xFF2E7D32)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (user.isMembershipActive) "Desactivar" else "Activar",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Text(
                    text = "Personal del Gimnasio (Acceso Operativo)",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}