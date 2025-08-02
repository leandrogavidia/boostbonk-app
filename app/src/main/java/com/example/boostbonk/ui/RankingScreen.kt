import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkYellow
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun RankingScreen() {
    val (selectedTab, setSelectedTab) = remember { mutableStateOf("This Week") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Tab Buttons
        Row {
            Button(
                onClick = { setSelectedTab("This Week") },

                modifier = Modifier.weight(1f)
            ) {
                Text("This Week")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { setSelectedTab("History") },

                modifier = Modifier.weight(1f)
            ) {
                Text("History")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Countdown Clock (only visible on "This Week")
        if (selectedTab == "This Week") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFEAD5))
                    .padding(8.dp)
            ) {
                Icon(Icons.Default.Schedule, contentDescription = "Clock")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Week ends in: 2d 22h 32m 18s", color = Color.Red)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Top Builders List (can vary by tab)
        TopBuildersList()
    }
}

@Composable
fun TopBuildersList() {
    val builders = listOf(
        Triple("@cryptodev_mike", "Developer", 2847),
        Triple("@nft_artist_sara", "Artist", 2156),
        Triple("@defi_founder_alex", "Founder", 1923),
        Triple("@solana_builder_jen", "Developer", 1687),
        Triple("@web3_designer_tom", "Designer", 1534),
        Triple("@dao_creator_lisa", "Founder", 1298)
    )

    Column {
        Text("🚀 Top Builders by Boosts", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        builders.forEachIndexed { index, (name, role, boosts) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("#${index + 1}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(name)
                            Text(role, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                    Text("${boosts} boosts", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingScreenPreview() {
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
            RankingScreen()        }
    }
}
