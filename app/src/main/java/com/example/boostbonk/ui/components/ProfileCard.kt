import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    username: String,
    description: String
) {
    val gradient = Brush.horizontalGradient(listOf(Color(0xFFFF9800), Color(0xFFFF5722)))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(BonkWhite)
    ) {
        Column(
            modifier = Modifier
                .background(BonkWhite)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = username,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /* TODO: share action */ },
                border = BorderStroke(1.dp, gradient),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(stringResource(R.string.share_profile), color = BonkOrange)
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                onClick = {},
                icon = Icons.Filled.ArrowOutward,
                backgroundColor = BonkOrange,
                text = stringResource(R.string.send_bonk),
                contentColor = BonkWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(value = "2,847", label = stringResource(R.string.total_boosts), gradient, Modifier.weight(1f))
                StatCard(value = "156,789", label = stringResource(R.string.total_bonk), gradient, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RankingCard(rank = "#1", label = stringResource(R.string.week_ranking), Modifier.weight(1f))
                RankingCard(rank = "#3", label = stringResource(R.string.all_time), Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String, gradient: Brush, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(brush = gradient, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.displaySmall,
            color = BonkWhite
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = BonkWhite,
        )
    }
}

@Composable
fun RankingCard(rank: String, label: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            rank,
            fontSize = 18.sp,
            style = MaterialTheme.typography.labelMedium,
            color = BonkOrange
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = BonkGray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileCardPreview() {
    BoostBonkTheme {
        ProfileCard(
            username = "@cryptodev_mike",
            description = "Building the future of DeFi on Solana. Passionate about creating tools that empower builders and foster community growth. Always learning, always building!"
        )
    }
}
