package com.example.gymud.screens.user



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymud.data.model.User
import com.example.gymud.screens.common.*
import com.example.gymud.viewmodel.TrainerViewModel

@Composable
fun TrainerListScreen(
    onTrainerSelected: (User) -> Unit,
    trainerViewModel: TrainerViewModel = viewModel()
) {
    val trainers by trainerViewModel.trainers.collectAsState()
    val isLoading by trainerViewModel.isLoading.collectAsState()
    val errorMessage by trainerViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .padding(20.dp)
    ) {
        ScreenTitle(
            "Our Trainers",
            "Meet our team and get in touch with them"
        )

        Spacer(modifier = Modifier.height(20.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GymudYellow)
                }
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Error loading trainers",
                    color = Color.Red
                )
            }

            trainers.isEmpty() -> {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "There are no registered coaches yet.",
                        color = Color.Gray
                    )
                }
            }

            else -> {
                Text(
                    "Trainers available: ${trainers.size}",
                    color = GymudYellow,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(trainers, key = { it.uid }) { trainer ->
                        TrainerCard(
                            trainer = trainer,
                            onClick = { onTrainerSelected(trainer) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrainerCard(
    trainer: User,
    onClick: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = GymudYellow,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trainer.name,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = trainer.email,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Coach",
                    color = GymudYellow,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GymudYellow,
                contentColor = Color.Black
            )
        ) {
            Icon(Icons.Default.Chat, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Contact coach")
        }
    }
}
