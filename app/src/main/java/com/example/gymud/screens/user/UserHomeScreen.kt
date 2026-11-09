
package com.example.gymud.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gymud.screens.common.*

@Composable
fun UserHomeScreen(
    userName: String,
    onNavigateToRoutine: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToMembership: () -> Unit,
    onNavigateToChat: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        // App name and logout button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GYMUD",
                color = GymudYellow,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = GymudYellow
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "Salir",
                    color = GymudYellow
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Welcome message
        Text(
            text = "¡Hola, ${userName}!",
            color = Color.White,
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Bienvenido de nuevo a tu espacio de entrenamiento.",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Training information
        Text(
            text = "Mi entrenamiento",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = GymudYellow,
                    modifier = Modifier.size(35.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Próxima rutina",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Pendiente de asignación",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Consulta tus rutinas y registra tus avances para llevar un control de tu actividad física.",
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Main menu
        Text(
            text = "Mis opciones",
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeOption(
                title = "Rutinas",
                icon = Icons.Default.FitnessCenter,
                onClick = onNavigateToRoutine,
                modifier = Modifier.weight(1f)
            )

            HomeOption(
                title = "Progreso",
                icon = Icons.Default.TrendingUp,
                onClick = onNavigateToProgress,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeOption(
                title = "Membresía",
                icon = Icons.Default.CreditCard,
                onClick = onNavigateToMembership,
                modifier = Modifier.weight(1f)
            )

            HomeOption(
                title = "Entrenador",
                icon = Icons.Default.Chat,
                onClick = onNavigateToChat,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "¡Cada entrenamiento cuenta!",
            color = GymudYellow,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun HomeOption(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GymudSurface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GymudYellow,
                modifier = Modifier.size(27.dp)
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}
