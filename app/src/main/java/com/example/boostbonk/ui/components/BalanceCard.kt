import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowOutward
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
import com.example.boostbonk.ui.theme.BonkBlack
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    balanceBonk: Double,
    balanceUSD: Double,
    onReceive: () -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BonkWhite)
            .padding(24.dp)
            .fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            CustomButton(
                icon = Icons.Filled.ArrowDownward,
                text = stringResource(R.string.receive),
                backgroundColor = BonkOrange,
                onClick = onReceive,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                icon = Icons.Filled.ArrowOutward,
                text = stringResource(R.string.withdraw),
                backgroundColor = BonkBlack,
                onClick = onSend,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BalanceCardPreview() {
    BoostBonkTheme {
        BalanceCard(
            balanceUSD = 10.00,
            balanceBonk = 10.00,
            onReceive = {},
            onSend = {},
        )
    }
}
