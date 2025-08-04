import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun WithdrawScreen(
    onSendClick: (amount: String, address: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.send_bonk),
            style = MaterialTheme.typography.displayMedium,
            color = BonkWhite
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = BonkWhite),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            AvailableBalance(
                balanceUSD = 10.00,
                balanceBonk = 10.00,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))


        WithdrawCard(onSendClick = ::handleSend)
    }
}

fun handleSend(amount: String, address: String) {
    // Validate, trigger transaction, etc.
}

@Preview(showBackground = true)
@Composable
fun WithdrawScreenPreview() {
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
            WithdrawScreen(
                onSendClick = ::handleSend
            )        }
    }
}
