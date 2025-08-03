import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.utils.getTimeUntilNextMonday
import kotlinx.coroutines.delay

@Composable
fun WeekCountdown() {
    val (remaining, setRemaining) = remember { mutableStateOf(getTimeUntilNextMonday()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            setRemaining(getTimeUntilNextMonday())
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {
        Icon(
            Icons.Default.Schedule,
            contentDescription = stringResource(R.string.clock),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Week ends in: $remaining", color = BonkOrange)
    }
}

