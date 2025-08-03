import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.boostbonk.ui.theme.BonkWhite

@Composable
fun StatCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.horizontalGradient(listOf(Color(0xFFFF9800), Color(0xFFFF5722)))

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