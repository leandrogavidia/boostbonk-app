package com.example.boostbonk

import BottomNavBar
import FeedScreen
import LoginScreen
import NavigationRoutes
import ProfileScreen
import RankingScreen
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

@Composable
fun BoostBonkApp(
    supabase: SupabaseClient,
    sender: ActivityResultSender,
    lifecycleScope: LifecycleCoroutineScope,
    viewModel: BoostBonkViewModel = viewModel()
) {
    val navController = rememberNavController()
    val sessionStatus = supabase.auth.sessionStatus.collectAsState(initial = null)
    val isLoggedIn = sessionStatus.value is SessionStatus.Authenticated
    val isLoading = sessionStatus.value == null
    val session = (sessionStatus.value as? SessionStatus.Authenticated)?.session

    LaunchedEffect(isLoggedIn) {
        viewModel.loadSession()

        if (isLoggedIn) {
            navController.navigate(NavigationRoutes.Feed.route) {
                popUpTo(NavigationRoutes.Login.route) { inclusive = true }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BonkOrange),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.boostbonk_logo),
                contentDescription = stringResource(R.string.boostbonk_logo),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }
        return
    }

    BoostBonkTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val showBars = currentRoute != null && currentRoute != NavigationRoutes.Login.route

                if (showBars) {
                    TopAppBarWithWallet(
                        isLoggedIn = true,
                        walletAddress = viewModel.connectedWalletAddressPublic.collectAsState().value,
                        onConnectWallet = {
                            lifecycleScope.launch {
                                val result = walletAdapter.connect(sender)
                                when (result) {
                                    is TransactionResult.Success -> {
                                        val pubKeyBytes = result.authResult.accounts.first().publicKey
                                        val pubKeyBase58 = Base58.encode(pubKeyBytes)
                                        viewModel.connectedWalletAddress.value = pubKeyBase58
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
                }

            },
            bottomBar = {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val showBars = currentRoute != null && currentRoute != NavigationRoutes.Login.route

                if (showBars) {
                    BottomNavBar(
                        navController = navController,
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
