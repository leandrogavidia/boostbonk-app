import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkBlack
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.viewmodel.BoostBonkViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Twitter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    supabase: SupabaseClient,
) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context, supabase) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.boostbonk_logo),
            contentDescription = stringResource(R.string.boostbonk_logo),
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.boostboonk),
            style = MaterialTheme.typography.displayLarge,
            color = BonkWhite
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.for_builders),
            style = MaterialTheme.typography.bodyLarge,
            color = BonkWhite,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(BonkWhite)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.join_the_builder_community),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                color = BonkBlack
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.build_reputation),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = BonkGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            SignInWithXButton(
                onClick = {
                    scope.launch {
                        authManager.signUpWithX().collect { response ->
                            when (response) {
                                is AuthResponse.Success -> {}
                                is AuthResponse.Error -> {
                                    Log.e("Auth", "Error: ${response.message}")
                                }
                            }
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.terms_of_service_and_privacy_policy),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = BonkGray
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.make_it_fun),
            modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge,
            color = BonkWhite,
            textAlign = TextAlign.Center
        )
    }
}

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val message: String?) : AuthResponse
}

class AuthManager(
    private val context: Context,
    supabaseClient: SupabaseClient
) {
    private val supabase = supabaseClient

    fun signUpWithX(): Flow<AuthResponse> = flow {
        try {
            supabase.auth.signInWith(
                provider = Twitter,
                redirectUrl = "boostbonk://auth/callback"
            )

            emit(AuthResponse.Success)
        } catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }
}

/* @Preview(
    showBackground = true,
    apiLevel = 34
)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    BoostBonkTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(BonkOrange, BonkYellow),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    )
                )
        ) {
            LoginScreen(navController = navController)
        }
    }
} */