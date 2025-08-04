package com.example.boostbonk

import BottomNavBar
import FeedScreen
import LoginScreen
import ProfileScreen
import RankingScreen
import Screen
import WalletScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.boostbonk.ui.components.TopAppBarWithWallet
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme
import com.example.boostbonk.utils.Base58
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.TransactionResult
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val supabase = SupabaseClientProvider.client
    private val viewModel: BoostBonkViewModel by viewModels()
    private lateinit var sender: ActivityResultSender


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sender = ActivityResultSender(this)

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
                    topBar = {
                        TopAppBarWithWallet(
                            isLoggedIn = sessionState.value,
                            walletAddress = viewModel.walletAddressPublic.collectAsState().value,
                            onConnectWallet = {
                                lifecycleScope.launch {
                                    val result = walletAdapter.connect(sender)

                                    when (result) {
                                        is TransactionResult.Success -> {
                                            val pubKeyBytes = result.authResult.accounts.first().publicKey
                                            val pubKeyBase58 = Base58.encode(pubKeyBytes)
                                            viewModel.walletAddress.value = pubKeyBase58
                                        }
                                        is TransactionResult.NoWalletFound -> {
                                            Log.e("Wallet", "No wallet found.")
                                        }
                                        is TransactionResult.Failure -> {
                                            Log.e("Wallet", "Failed to connect: ${result.e.message}")
                                        }
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                        if (sessionState.value && currentRoute != Screen.Login.route) {
                            BottomNavBar(
                                navController,
                                currentUsername = viewModel.username.collectAsState().value ?: ""
                            )
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
                                FeedScreen(
                                    viewModel = viewModel,
                                    navController = navController,
                                    sender = sender
                                )
                            }
                            composable(Screen.Wallet.route) {
                                WalletScreen()
                            }
                            composable("profile/{username}") { backStackEntry ->
                                val username = backStackEntry.arguments?.getString("username")
                                LaunchedEffect(username) {
                                    viewModel.loadUserProfileByUsername(username)
                                }

                                val userInfo = viewModel.selectedUser.collectAsState().value

                                ProfileScreen(
                                    viewModel = viewModel,
                                    userInfo = userInfo,
                                    navController = navController,
                                    sender = sender
                                )
                            }
                            composable(Screen.Ranking.route) {
                                RankingScreen(
                                    viewModel = viewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
