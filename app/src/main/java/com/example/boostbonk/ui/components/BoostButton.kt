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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.ui.theme.BoostBonkTheme

@Composable
fun BoostButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors( containerColor = if (enabled) BonkOrange else BonkGray),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled
    ) {
        Icon(
            Icons.Filled.RocketLaunch,
            contentDescription = null,
            tint = BonkWhite
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (enabled) stringResource(R.string.boost) else stringResource(R.string.no_wallet),
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
            BoostButton(
                enabled = false,
                onClick = {}
            )
        }
    }
}

