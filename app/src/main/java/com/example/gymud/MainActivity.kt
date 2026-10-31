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
import com.example.gymud.screens.admin.*
import com.example.gymud.screens.auth.*
import com.example.gymud.screens.trainer.*
import com.example.gymud.screens.user.*
import com.example.gymud.viewmodel.AuthState
import com.example.gymud.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val authState by authViewModel.authState.collectAsState()
                    val context = LocalContext.current

                    var currentUserName by remember { mutableStateOf("Usuario") }

                    LaunchedEffect(authState) {
                        when (authState) {
                            is AuthState.Success -> {
                                val uid = (authState as AuthState.Success).uid
                                FirebaseFirestore.getInstance().collection("users").document(uid).get()
                                    .addOnSuccessListener { document ->
                                        val role = document.getString("role") ?: "USER"
                                        currentUserName = document.getString("name") ?: "Usuario"

                                        // Validacion de los 3 roles
                                        val destination = when (role) {
                                            "ADMIN" -> "admin_home"
                                            "TRAINER" -> "trainer_home"
                                            else -> "user_home"
                                        }

                                        navController.navigate(destination) {
                                            popUpTo("login") { inclusive = true }
                                        }
                                        authViewModel.resetState()
                                    }
                                    .addOnFailureListener {
                                        navController.navigate("user_home") { popUpTo("login") { inclusive = true } }
                                        authViewModel.resetState()
                                    }
                            }
                            is AuthState.Error -> {
                                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_LONG).show()
                                authViewModel.resetState()
                            }
                            else -> {}
                        }
                    }

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginClick = { e, p -> authViewModel.login(e, p) },
                                onNavigateToRegister = { navController.navigate("register") },
                                onNavigateToForgotPassword = { navController.navigate("forgot_password") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegisterClick = { n, e, p -> authViewModel.register(n, e, p) },
                                onNavigateToLogin = { navController.popBackStack() }
                            )
                        }
                        composable("forgot_password") { ForgotPasswordScreen { navController.popBackStack() } }

                        // --- USER ---
                        composable("user_home") {
                            UserHomeScreen(
                                userName = currentUserName,
                                onNavigateToRoutine = { navController.navigate("user_routine") },
                                onNavigateToProgress = { navController.navigate("user_progress") },
                                onNavigateToMembership = { navController.navigate("user_membership") },
                                onNavigateToChat = { navController.navigate("chat_screen") }
                            )
                        }
                        composable("user_routine") { UserRoutineScreen() }
                        composable("user_progress") { ProgressScreen() }
                        composable("user_membership") { MembershipScreen() }

                        // --- TRAINER ---
                        composable("trainer_home") {
                            TrainerHomeScreen(
                                trainerName = currentUserName,
                                onNavigateToAssign = { navController.navigate("assign_routine") },
                                onNavigateToChat = { navController.navigate("chat_screen") }
                            )
                        }
                        composable("assign_routine") { AssignRoutineScreen() }
                        composable("chat_screen") { ChatScreen() } // Compartido entre Trainer y User

                        // --- ADMIN ---
                        composable("admin_home") {
                            AdminDashboardScreen(
                                onNavigateToUsers = { navController.navigate("admin_users") },
                                onNavigateToInventory = { navController.navigate("admin_inventory") }
                            )
                        }
                        composable("admin_users") { UserManagementScreen() }
                        composable("admin_inventory") { InventoryScreen() }
                    }
                }
            }
        }
    }
}