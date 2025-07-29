import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun ReceiveScreen(
    modifier: Modifier = Modifier,
    walletAddress: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.receive_bonk),
            style = MaterialTheme.typography.displayMedium,
            color = BonkWhite
        )
        Spacer(modifier = Modifier.height(16.dp))
        ReceiveCard(
            walletAddress = walletAddress
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiveScreenPreview() {
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
            ReceiveScreen(walletAddress = "7xKXtg2CW87d97TXJSDpbD5jBkheTqA83TZRuJosgAsU")
        }
    }
}
