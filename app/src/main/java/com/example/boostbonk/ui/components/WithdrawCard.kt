import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite

@Composable
fun WithdrawCard(
    modifier: Modifier = Modifier,
    onSendClick: (amount: String, address: String) -> Unit
) {
    val (amount, setAmount) = remember { mutableStateOf("") }
    val (address, setAddress) = remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BonkWhite),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(R.string.amount_bonk),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { setAmount(it) },
                placeholder = { Text(stringResource(R.string.enter_amount_to_send)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(stringResource(R.string.receiver_wallet_address), fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { setAddress(it) },
                placeholder = { Text(stringResource(R.string.enter_receiver_wallet_address)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(Modifier.height(20.dp))

            CustomButton(
                onClick = { onSendClick(amount, address) },
                icon = Icons.Filled.ArrowOutward,
                backgroundColor = BonkOrange,
                text = stringResource(R.string.send_bonk),
                contentColor = BonkWhite,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.send_bonk_warning),
                style = MaterialTheme.typography.bodyMedium,
                color = BonkGray,
                modifier = Modifier
                    .background(Color(0xFFFFFFCC),
                        RoundedCornerShape(6.dp))
                    .padding(12.dp),
                textAlign = TextAlign.Center
            )
        }
}}