import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.data.mock.mockPosts
import com.example.boostbonk.data.model.Post
import com.example.boostbonk.ui.components.ColumnValue
import com.example.boostbonk.ui.theme.BonkBlack
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: Post,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BonkWhite)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BonkGray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = post.username,
                        style = MaterialTheme.typography.labelLarge,
                        color = BonkBlack
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    text = post.created_at ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BonkGray
                )
            }
        }

        Text(
            text = post.description,
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = BonkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFECECEC)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AccountBox, contentDescription = null, tint = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(BonkGray, shape = RoundedCornerShape(10.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ColumnValue(
                    name = "Boosts",
                    value = post.boosts
                )
            }
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ColumnValue(
                    name = "BONK EARNED",
                    value = post.bonk_earned
                )
            }
            BoostButton()
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = false)
@Composable
fun PostCardPreview() {
    BoostBonkTheme {
        Box(modifier = Modifier) {
            PostCard(
                post = mockPosts.get(0)
            )
        }
    }
}

