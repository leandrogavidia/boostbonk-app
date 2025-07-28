import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun BoostButton(
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /* Boost click */ },
        colors = ButtonDefaults.buttonColors(containerColor = BonkOrange),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            Icons.Filled.RocketLaunch,
            contentDescription = null,
            tint = BonkWhite
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Boost",
            color = BonkWhite,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Preview(showBackground = false)
@Composable
fun ColumnValuePreview() {
    BoostBonkTheme {
        Box(modifier = Modifier) {
            BoostButton()
        }
    }
}

