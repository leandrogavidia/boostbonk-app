package com.example.boostbonk

import BottomNavBar
import FeedScreen
import LoginScreen
import ProfileScreen
import Screen
import WalletScreen
import RankingScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.*
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
            val sessionState = remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                val session = supabase.auth.currentSessionOrNull()
                if (session != null) {
                    sessionState.value = true
                    navController.navigate(Screen.Feed.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            BoostBonkTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        if (sessionState.value && currentRoute != Screen.Login.route) {
                            BottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
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
                        NavHost(navController, startDestination = Screen.Login.route) {
                            composable(Screen.Login.route) {
                                LoginScreen(
                                    navController = navController,
                                    supabase = supabase,
                                )
                            }
                            composable(Screen.Feed.route) {
                                FeedScreen()
                            }
                            composable(Screen.Wallet.route) {
                                WalletScreen()
                            }
                            composable(Screen.Profile.route) {
                                ProfileScreen()
                            }
                            composable(Screen.Ranking.route) {
                                RankingScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
