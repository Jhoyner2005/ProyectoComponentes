package com.example.gymud.screens.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    val users by adminViewModel.users.collectAsState()
    val message by adminViewModel.message.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            adminViewModel.clearMessage()
        }
    }

    val filteredUsers = users.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.email.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().background(GymudDark).padding(20.dp)) {
        ScreenTitle("Gestión de Miembros", "Administrar roles y membresías")

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar por nombre o correo...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, tint = GymudYellow, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = GymudSurface,
                focusedContainerColor = GymudSurface,
                focusedIndicatorColor = GymudYellow,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredUsers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No se encontraron usuarios", color = Color.Gray)
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
                        color = if (user.isMembershipActive) Color.Green else Color.Red,
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