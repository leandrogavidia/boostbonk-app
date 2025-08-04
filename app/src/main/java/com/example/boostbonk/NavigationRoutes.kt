sealed class NavigationRoutes(val route: String, val label: String) {
    object Login : NavigationRoutes("login", "Login")
    object Feed : NavigationRoutes("feed", "Feed")
    object Profile : NavigationRoutes("profile", "Profile")
    // object Wallet : Screen("wallet", "Wallet")
    object Ranking : NavigationRoutes("ranking", "Ranking")
}