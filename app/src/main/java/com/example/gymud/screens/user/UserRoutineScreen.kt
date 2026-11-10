package com.example.gymud.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gymud.data.model.Exercise
import com.example.gymud.screens.common.*

@Composable
fun UserRoutineScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }

    val categories = listOf("Todas", "Piernas", "Brazos", "Pecho", "Espalda", "Hombros", "Abdomen")

    // Lista de ejercicios
    val sampleExercises = remember {
        listOf(
            // PIERNAS
            Exercise(
                id = "p1",
                name = "Sentadilla Libre con Barra",
                description = "Ejercicio multiarticular enfocado en el desarrollo de cuadriceps, gluteos e isquiotibiales.",
                category = "Piernas",
                imageUrl = "https://images.unsplash.com/photo-1574680096145-d05b474e2155?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "p2",
                name = "Prensa de Piernas Inclinada",
                description = "Enfoca la carga de trabajo en el tren inferior reduciendo la presion lumbar.",
                category = "Piernas",
                imageUrl = "https://images.unsplash.com/photo-1434682881908-b43d0467b798?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "p3",
                name = "Peso Muerto Rumano",
                description = "Excelente ejercicio enfocado en la cadena posterior: isquiotibiales y gluteos.",
                category = "Piernas",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 12
            ),
            Exercise(
                id = "p4",
                name = "Zancadas con Mancuernas",
                description = "Ejercicio unilateral ideal para corregir desbalance muscular y dar forma a los gluteos.",
                category = "Piernas",
                imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 12
            ),
            Exercise(
                id = "p5",
                name = "Extension de Cuadriceps",
                description = "Aislamiento directo para definir y tonificar la parte frontal del muslo.",
                category = "Piernas",
                imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 15
            ),
            Exercise(
                id = "p6",
                name = "Elevacion de Talones",
                description = "Desarrollo y definición de los gemelos y soleos en la pantorrilla.",
                category = "Piernas",
                imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 20
            ),

            // BRAZOS
            Exercise(
                id = "b1",
                name = "Curl de Bíceps con Barra",
                description = "Ejercicio clasico para maximizar el volumen y fuerza de los bíceps.",
                category = "Brazos",
                imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "b2",
                name = "Extensiones de Tríceps",
                description = "Aislamiento de la cabeza lateral y larga del tríceps con polea alta.",
                category = "Brazos",
                imageUrl = "https://images.unsplash.com/photo-1583454110551-21f2fa2afe61?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "b3",
                name = "Curl Martillo Mancuernas",
                description = "Trabaja el braquial y braquiorradial para dar mayor grosor al brazo.",
                category = "Brazos",
                imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 10
            ),
            Exercise(
                id = "b4",
                name = "Press Frances Barra Z",
                description = "Ejercicio fundamental para ganar masa muscular y potencia en los tríceps.",
                category = "Brazos",
                imageUrl = "https://images.unsplash.com/photo-1583454110551-21f2fa2afe61?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "b5",
                name = "Curl Concentrado",
                description = "Aislamiento enfocado en esculpir el pico del bíceps.",
                category = "Brazos",
                imageUrl = "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 12
            ),
            Exercise(
                id = "b6",
                name = "Fondos en Banco",
                description = "Ejercicio de peso corporal enfocado en tonificar tríceps y hombros.",
                category = "Brazos",
                imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 15
            ),

            // PECHO
            Exercise(
                id = "c1",
                name = "Press de Banca Plano",
                description = "El basico por excelencia para la construccion de la masa pectoral mayor.",
                category = "Pecho",
                imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 8
            ),
            Exercise(
                id = "c2",
                name = "Press Inclinado Mancuernas",
                description = "Enfocado en la porción superior del pectoral para lograr un pecho balanceado.",
                category = "Pecho",
                imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "c3",
                name = "Aperturas en Polea",
                description = "Otorga estiramiento maximo y definicion en la fisura central del pecho.",
                category = "Pecho",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 12
            ),
            Exercise(
                id = "c4",
                name = "Press Declinado con Barra",
                description = "Enfocado en la zona inferior del pecho para delinear el contorno pectoral.",
                category = "Pecho",
                imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "c5",
                name = "Flexiones de Pecho",
                description = "Básico y efectivo ejercicio corporal para tono pectoral y firmeza del core.",
                category = "Pecho",
                imageUrl = "https://images.unsplash.com/photo-1598971639058-fab3c3109a00?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 15
            ),
            Exercise(
                id = "c6",
                name = "Pullover con Mancuerna",
                description = "Amplía la caja toracica y activa las fibras superiores del pectoral.",
                category = "Pecho",
                imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 12
            ),

            // ESPALDA
            Exercise(
                id = "e1",
                name = "Dominadas Abiertas",
                description = "Desarrolla la anchura de la espalda y fortalece los dorsales.",
                category = "Espalda",
                imageUrl = "https://images.unsplash.com/photo-1605296867304-46d5465a13f1?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 8
            ),
            Exercise(
                id = "e2",
                name = "Remo con Barra",
                description = "Aumenta la densidad y grosor de la zona media y superior de la espalda.",
                category = "Espalda",
                imageUrl = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "e3",
                name = "Jalon al Pecho en Polea",
                description = "Alternativa excelente a las dominadas para moldear los dorsales anchos.",
                category = "Espalda",
                imageUrl = "https://images.unsplash.com/photo-1605296867304-46d5465a13f1?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "e4",
                name = "Remo Gironda Polea Baja",
                description = "Excelente estímulo enfocado en romboides, trapecios e infraespinosos.",
                category = "Espalda",
                imageUrl = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "e5",
                name = "Peso Muerto Convencional",
                description = "Ejercicio de fuerza total para toda la cadena posterior y zona lumbar.",
                category = "Espalda",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 6
            ),
            Exercise(
                id = "e6",
                name = "Hiperextensiones Lumbares",
                description = "Fortalecimiento preventivo de la zona baja de la espalda y postureo.",
                category = "Espalda",
                imageUrl = "https://images.unsplash.com/photo-1526506118085-60ce8714f8c5?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 15
            ),

            // HOMBROS
            Exercise(
                id = "h1",
                name = "Press Militar Mancuernas",
                description = "Desarrollo de los deltoides anteriores y laterales en posicion sentada.",
                category = "Hombros",
                imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "h2",
                name = "Elevaciones Laterales",
                description = "Ejercicio de aislamiento clave para dar amplitud a los deltoides laterales.",
                category = "Hombros",
                imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 15
            ),
            Exercise(
                id = "h3",
                name = "Elevaciones Frontales",
                description = "Aislamiento directo enfocado en la cabeza anterior del hombro.",
                category = "Hombros",
                imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 12
            ),
            Exercise(
                id = "h4",
                name = "Pájaros con Mancuerna",
                description = "Aislamiento del deltoides posterior para dar un aspecto 3D y redondo al hombro.",
                category = "Hombros",
                imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "h5",
                name = "Press Arnold",
                description = "Rotacion dinamica que involucra las tres caras del deltoides.",
                category = "Hombros",
                imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 10
            ),
            Exercise(
                id = "h6",
                name = "Encogimiento de Hombros",
                description = "Enfocado en el fortalecimiento y masa del musculo trapecio.",
                category = "Hombros",
                imageUrl = "https://images.unsplash.com/photo-1541534741688-6078c6bfb5c5?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 15
            ),

            // ABDOMEN
            Exercise(
                id = "a1",
                name = "Crunch Abdominal",
                description = "Ejercicio clásico de encogimiento para la zona superior del recto abdominal.",
                category = "Abdomen",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 20
            ),
            Exercise(
                id = "a2",
                name = "Plancha Isometrica",
                description = "Fortalecimiento estatico del core, estabilizando la columna y pelvis.",
                category = "Abdomen",
                imageUrl = "https://images.unsplash.com/photo-1598971639058-fab3c3109a00?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 45
            ),
            Exercise(
                id = "a3",
                name = "Elevacion de Piernas",
                description = "Ejercicio exigente enfocado en la porcion inferior del area abdominal.",
                category = "Abdomen",
                imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 12
            ),
            Exercise(
                id = "a4",
                name = "Rueda Abdominal (Ab Roller)",
                description = "Trabajo de alta tensión e intensidad para toda la pared abdominal y core.",
                category = "Abdomen",
                imageUrl = "https://images.unsplash.com/photo-1517838277536-f5f99be501cd?q=80&w=600&auto=format&fit=crop",
                sets = 3,
                reps = 10
            ),
            Exercise(
                id = "a5",
                name = "Giros Rusos (Russian Twists)",
                description = "Rotacion de tronco enfocada en tonificar los musculos oblicuos laterales.",
                category = "Abdomen",
                imageUrl = "https://images.unsplash.com/photo-1598971639058-fab3c3109a00?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 20
            ),
            Exercise(
                id = "a6",
                name = "Mountain Climbers",
                description = "Ejercicio dinamico de abdomen y resistencia cardiovascular.",
                category = "Abdomen",
                imageUrl = "https://images.unsplash.com/photo-1434682881908-b43d0467b798?q=80&w=600&auto=format&fit=crop",
                sets = 4,
                reps = 30
            )
        )
    }

    // Filtrar por categorías
    val filteredExercises = remember(searchQuery, selectedCategory) {
        sampleExercises.filter { exercise ->
            val matchesCategory = selectedCategory == "Todas" || exercise.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = exercise.name.contains(searchQuery, ignoreCase = true) ||
                    exercise.category.contains(searchQuery, ignoreCase = true) ||
                    exercise.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GymudDark)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        ScreenTitle("Tus Rutinas", "Explora ejercicios por grupo muscular")

        // Barra de busqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar ejercicios o musculos...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, tint = GymudYellow, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, tint = Color.Gray, contentDescription = "Limpiar")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = GymudSurface,
                focusedContainerColor = GymudSurface,
                focusedIndicatorColor = GymudYellow,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // Chips de Filtro de Categoría
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = category },
                    label = {
                        Text(
                            text = category,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else Color.White
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GymudYellow,
                        containerColor = GymudSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Color.White.copy(alpha = 0.2f),
                        selectedBorderColor = GymudYellow
                    )
                )
            }
        }

        // Contador de resultados
        Text(
            text = "${filteredExercises.size} ejercicios disponibles",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Lista / Tarjetas de Ejercicios
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredExercises, key = { it.id }) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onClick = { selectedExercise = exercise }
                )
            }
        }
    }

    // Modal / detalle de ejercicio
    selectedExercise?.let { exercise ->
        AlertDialog(
            onDismissRequest = { selectedExercise = null },
            containerColor = GymudSurface,
            title = {
                Text(
                    text = exercise.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black.copy(alpha = 0.3f))
                    ) {
                        AsyncImage(
                            model = exercise.imageUrl,
                            contentDescription = exercise.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Categoría: ${exercise.category.uppercase()}",
                        color = GymudYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = exercise.description,
                        color = Color.LightGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = GymudYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Series: ${exercise.sets}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Repeat,
                                    contentDescription = null,
                                    tint = GymudYellow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (exercise.category == "Abdomen" && exercise.name.contains("Plancha")) "${exercise.reps} seg" else "Reps: ${exercise.reps}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedExercise = null }) {
                    Text("Cerrar", color = GymudYellow, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            // Imagen del ejercicio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.08f))
            ) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = exercise.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = exercise.category.uppercase(),
                        color = GymudYellow,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Nombre del ejercicio
            Text(
                text = exercise.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Series y Repeticiones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${exercise.sets} series",
                    color = Color.LightGray,
                    fontSize = 11.sp
                )
                Text(
                    text = if (exercise.category == "Abdomen" && exercise.name.contains("Plancha")) "${exercise.reps} seg" else "${exercise.reps} reps",
                    color = GymudYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
