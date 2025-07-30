import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun AvailableBalance(
    modifier: Modifier = Modifier.fillMaxWidth(),
    balanceBonk: Double,
    balanceUSD: Double,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.bonk_coin),
            contentDescription = stringResource(R.string.bonk_coin),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.total_balance),
            style = MaterialTheme.typography.displaySmall,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
            color = BonkGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = String.format("%,.2f", balanceBonk),
            style = MaterialTheme.typography.displayMedium,
            color = BonkOrange,
            letterSpacing = 2.sp,
        )

        Text(
            text = stringResource(R.string.bonk),
            style = MaterialTheme.typography.labelLarge,
            color = BonkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${String.format("%,.2f", balanceUSD)} USD",
            style = MaterialTheme.typography.bodyMedium,
            color = BonkGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AvailableBalancePreview() {
    BoostBonkTheme {
        AvailableBalance(
            balanceUSD = 10.00,
            balanceBonk = 10.00,
        )
    }
}