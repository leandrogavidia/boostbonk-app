package com.example.boostbonk

import BottomNavBar
import FeedScreen
import LoginScreen
import ProfileScreen
import RankingScreen
import NavigationRoutes
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.boostbonk.solana.walletAdapter
import com.example.boostbonk.ui.components.TopAppBarWithWallet
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme
import com.example.boostbonk.utils.Base58
import com.example.boostbonk.viewmodel.BoostBonkViewModel
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.TransactionResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.launch

@Composable
fun BoostBonkApp(
    supabase: SupabaseClient,
    sender: ActivityResultSender,
    lifecycleScope: LifecycleCoroutineScope,
    viewModel: BoostBonkViewModel = viewModel()
) {
    val navController = rememberNavController()
    val currentSessionState = produceState<UserSession?>(initialValue = null) {
        value = supabase.auth.currentSessionOrNull()
    }
    val currentSession = currentSessionState.value
    val isLoggedIn = currentSession != null

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(NavigationRoutes.Feed.route) {
                popUpTo(NavigationRoutes.Login.route) { inclusive = true }
            }
        }
    }

    BoostBonkTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBarWithWallet(
                    isLoggedIn = isLoggedIn,
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
                if (isLoggedIn && currentRoute != NavigationRoutes.Login.route) {
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
                NavHost(navController, startDestination = NavigationRoutes.Login.route) {
                    composable(NavigationRoutes.Login.route) {
                        LoginScreen(
                            navController = navController,
                            supabase = supabase,
                        )
                    }
                    composable(NavigationRoutes.Feed.route) {
                        FeedScreen(
                            viewModel = viewModel,
                            navController = navController,
                            sender = sender
                        )
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
                    composable(NavigationRoutes.Ranking.route) {
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
