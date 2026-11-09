
package com.example.gymud

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymud.data.model.User
import com.example.gymud.screens.admin.*
import com.example.gymud.screens.auth.*
import com.example.gymud.screens.trainer.*
import com.example.gymud.screens.user.*
import com.example.gymud.viewmodel.AdminViewModel
import com.example.gymud.viewmodel.AuthState
import com.example.gymud.viewmodel.AuthViewModel
import com.example.gymud.viewmodel.ProgressViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()
                    val context = LocalContext.current

                    var currentUserName by remember {
                        mutableStateOf("Usuario")
                    }

                    var currentUserId by remember {
                        mutableStateOf("")
                    }

                    var selectedTrainer by remember {
                        mutableStateOf<User?>(null)
                    }

                    LaunchedEffect(authState) {
                        when (authState) {
                            is AuthState.Success -> {
                                val uid = (authState as AuthState.Success).uid

                                currentUserId = uid

                                FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener { document ->

                                        val role = document.getString("role") ?: "USER"
                                        currentUserName =
                                            document.getString("name") ?: "Usuario"

                                        val destination = when (role) {
                                            "ADMIN" -> "admin_home"
                                            "TRAINER" -> "trainer_home"
                                            else -> "user_home"
                                        }

                                        navController.navigate(destination) {
                                            popUpTo("login") {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }

                                        authViewModel.resetState()
                                    }
                                    .addOnFailureListener {
                                        navController.navigate("user_home") {
                                            popUpTo("login") {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }

                                        authViewModel.resetState()
                                    }
                            }

                            is AuthState.PasswordResetSent -> {
                                Toast.makeText(
                                    context,
                                    "A password reset link was sent to your email address.",
                                    Toast.LENGTH_LONG
                                ).show()

                                authViewModel.resetState()
                                navController.popBackStack()
                            }

                            is AuthState.Error -> {
                                Toast.makeText(
                                    context,
                                    (authState as AuthState.Error).message,
                                    Toast.LENGTH_LONG
                                ).show()

                                authViewModel.resetState()
                            }

                            else -> {}
                        }
                    }

                    val isLoading = authState is AuthState.Loading

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {

                        // AUTH
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    authViewModel.login(email, password)
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                },
                                onNavigateToForgotPassword = {
                                    navController.navigate("forgot_password")
                                },
                                isLoading = isLoading
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterClick = { name, email, password ->
                                    authViewModel.register(name, email, password)
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                },
                                isLoading = isLoading
                            )
                        }

                        composable("forgot_password") {
                            ForgotPasswordScreen(
                                onSendResetClick = { email ->
                                    authViewModel.resetPassword(email)
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                isLoading = isLoading
                            )
                        }

                        // USER
                        composable("user_home") {
                            UserHomeScreen(
                                userName = currentUserName,
                                onNavigateToRoutine = {
                                    navController.navigate("user_routine")
                                },
                                onNavigateToProgress = {
                                    navController.navigate("user_progress")
                                },
                                onNavigateToMembership = {
                                    navController.navigate("user_membership")
                                },
                                onNavigateToChat = {
                                    navController.navigate("trainer_list")
                                },
                                onLogout = {
                                    authViewModel.logout()

                                    currentUserId = ""
                                    selectedTrainer = null

                                    navController.navigate("login") {
                                        popUpTo("login") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable("user_routine") {
                            UserRoutineScreen()
                        }

                        composable("user_progress") {
                            val progressViewModel: ProgressViewModel = viewModel()

                            ProgressScreen(
                                userId = currentUserId,
                                progressViewModel = progressViewModel
                            )
                        }

                        composable("user_membership") {
                            MembershipScreen()
                        }

                        // TRAINER DIRECTORY
                        composable("trainer_list") {
                            TrainerListScreen(
                                onTrainerSelected = { trainer ->
                                    selectedTrainer = trainer
                                    navController.navigate("chat_screen")
                                }
                            )
                        }

                        // TRAINER
                        composable("trainer_home") {
                            TrainerHomeScreen(
                                trainerName = currentUserName,
                                onNavigateToAssign = {
                                    navController.navigate("assign_routine")
                                },
                                onNavigateToChat = {
                                    navController.navigate("chat_screen")
                                },
                                onLogout = {
                                    authViewModel.logout()
                                    currentUserId = ""
                                    selectedTrainer = null
                                    navController.navigate("login") {
                                        popUpTo("login") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        composable("assign_routine") {
                            AssignRoutineScreen()
                        }

                        composable("chat_screen") {
                            ChatScreen(
                                userId = currentUserId,
                                otherUserId = selectedTrainer?.uid ?: "",
                                otherUserName = selectedTrainer?.name ?: "Messages",
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // ADMIN
                        composable("admin_home") {
                            val adminViewModel: AdminViewModel = viewModel()

                            AdminDashboardScreen(
                                onNavigateToUsers = {
                                    navController.navigate("admin_users")
                                },
                                onNavigateToInventory = {
                                    navController.navigate("admin_inventory")
                                },
                                onLogout = {
                                    authViewModel.logout()
                                    currentUserId = ""
                                    selectedTrainer = null
                                    navController.navigate("login") {
                                        popUpTo("login") {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                adminViewModel = adminViewModel
                            )
                        }

                        composable("admin_users") {
                            val adminViewModel: AdminViewModel = viewModel()

                            UserManagementScreen(
                                adminViewModel = adminViewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("admin_inventory") {
                            val adminViewModel: AdminViewModel = viewModel()

                            InventoryScreen(
                                adminViewModel = adminViewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
