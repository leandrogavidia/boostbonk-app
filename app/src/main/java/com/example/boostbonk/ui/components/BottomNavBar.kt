import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
// import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange

@Composable
fun BottomNavBar(
    navController: NavHostController,
    currentUsername: String
) {
    val items = listOf(NavigationRoutes.Feed, NavigationRoutes.Ranking/*, Screen.Wallet*/, NavigationRoutes.Profile)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            val isSelected = when (screen) {
                is NavigationRoutes.Profile -> currentRoute?.startsWith("profile") == true
                else -> currentRoute == screen.route
            }

            val route = when (screen) {
                is NavigationRoutes.Profile -> "profile/$currentUsername"
                else -> screen.route
            }

            val icon = when (screen) {
                is NavigationRoutes.Feed -> Icons.Filled.Home
                is NavigationRoutes.Ranking -> Icons.Filled.EmojiEvents
                // is Screen.Wallet -> Icons.Filled.Wallet
                is NavigationRoutes.Profile -> Icons.Filled.Person
                else -> Icons.Filled.Home
            }

            val label = screen.label

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(NavigationRoutes.Feed.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(icon, contentDescription = label)
                },
                label = { Text(label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BonkOrange,
                    unselectedIconColor = BonkGray,
                    selectedTextColor = BonkOrange,
                    unselectedTextColor = BonkGray
                )
            )
        }
    }
}