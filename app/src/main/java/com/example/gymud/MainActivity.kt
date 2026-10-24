package com.example.gymud

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymud.screens.auth.LoginScreen
import com.example.gymud.screens.auth.RegisterScreen
import com.example.gymud.viewmodel.AuthState
import com.example.gymud.viewmodel.AuthViewModel

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

                    LaunchedEffect(authState) {
                        when (authState) {
                            is AuthState.Success -> {
                                authViewModel.resetState()
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                            is AuthState.Error -> {
                                val errorMessage = (authState as AuthState.Error).message
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                                authViewModel.resetState()
                            }
                            else -> {}
                        }
                    }
                    NavHost(navController = navController, startDestination = "login") {

                        composable("login") {
                            LoginScreen(
                                onLoginClick = { email, password ->
                                    authViewModel.login(email, password)
                                },
                                onNavigateToRegister = {
                                    navController.navigate("register")
                                },
                                onNavigateToForgotPassword = {
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                onRegisterClick = { name, email, password ->
                                    authViewModel.register(name, email, password)
                                },
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("home") {
                            Surface(modifier = Modifier.fillMaxSize()) {
                                Text(text = "¡Bienvenido a GYMUD! Has iniciado sesión correctamente.")
                            }
                        }
                    }
                }
            }
        }
    }
}
