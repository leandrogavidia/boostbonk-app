sealed class Screen(val route: String, val label: String) {
    object Login : Screen("login", "Login")
    object Feed : Screen("feed", "Feed")
    object Profile : Screen("profile", "Profile")
    object Wallet : Screen("wallet", "Wallet")
    object Ranking : Screen("ranking", "Ranking")
}