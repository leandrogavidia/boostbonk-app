package com.example.boostbonk

import FeedScreen
import LoginScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme
import io.github.jan.supabase.auth.auth

class MainActivity : ComponentActivity() {

    private val supabase = SupabaseClientProvider.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                val session = supabase.auth.currentSessionOrNull()
                if (session != null) {
                    navController.navigate("feed") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            BoostBonkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(BonkOrange, BonkYellow),
                                    start = Offset(0f, 0f),
                                    end = Offset.Infinite
                                )
                            )
                            .padding(innerPadding)
                    ) {
                        NavHost(navController, startDestination = "login") {
                            composable("login") {
                                LoginScreen(navController = navController, supabase = supabase)
                            }
                            composable("feed") {
                                FeedScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
