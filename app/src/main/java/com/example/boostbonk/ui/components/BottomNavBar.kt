import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(Screen.Feed, Screen.Ranking, Screen.Wallet, Screen.Profile)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Feed.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                label = { Text(screen.label) },
                icon = {
                    when (screen) {
                        is Screen.Feed -> Icon( Icons.Filled.Home, contentDescription = stringResource(
                            R.string.feed
                        ))
                        is Screen.Ranking -> Icon(Icons.Filled.EmojiEvents, contentDescription = stringResource(
                            R.string.ranking
                        ))
                        is Screen.Wallet -> Icon(Icons.Filled.Wallet, contentDescription = stringResource(
                            R.string.wallet
                        ))
                        is Screen.Profile -> Icon(Icons.Filled.Person, contentDescription = stringResource(
                            R.string.profile
                        ))
                        else -> {}
                    }
                },
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