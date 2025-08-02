import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkBlack
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun ReceiveCard(
    walletAddress: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = BonkWhite)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .border(2.dp, BonkOrange, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.QrCode,
                    contentDescription = stringResource(R.string.qr_code),
                    tint = BonkOrange,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.your_wallet_address),
                style = MaterialTheme.typography.labelMedium,
                color = BonkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BonkGray, RoundedCornerShape(8.dp))
                    .padding(12.dp),
            ) {
                Text(
                    text = walletAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BonkWhite,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            CustomButton(
                onClick = {},
                contentColor = BonkWhite,
                backgroundColor = BonkOrange,
                text = stringResource(R.string.copy_address),
                icon = Icons.Filled.ContentCopy,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.receive_warning),
                style = MaterialTheme.typography.bodyMedium,
                color = BonkBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color(0xFFFFF2B2), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiveCardPreview() {
    BoostBonkTheme {
        ReceiveCard(
            walletAddress = "7xKXtg2CW87d97TXJSDpbD5jBkheTqA83TZRuJosgAsU"
        )
    }
}
