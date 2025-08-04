import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.boostbonk.viewmodel.BoostBonkViewModel
import com.example.boostbonk.R
import com.example.boostbonk.data.model.UserInfo
import com.example.boostbonk.solana.sendBonkFunctionRequest
import com.example.boostbonk.solana.walletAdapter
import com.example.boostbonk.ui.theme.BonkGray
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import com.example.boostbonk.utils.formatBonkEarned
import com.funkatronics.encoders.Base58
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.TransactionResult
import com.solana.mobilewalletadapter.clientlib.successPayload
import kotlinx.coroutines.launch

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    displayName: String,
    username: String,
    avatarUrl: String,
    isOwnProfile: Boolean,
    viewModel: BoostBonkViewModel,
    fromUserId: String,
    toUserId: String,
    totalBoosts: Int,
    totalBonkEarned: Double,
    sender: ActivityResultSender,
    userInfo: UserInfo? = null,
    ) {
    val coroutineScope = rememberCoroutineScope()

    val (showBoostModal, setShowBoostModal) = remember { mutableStateOf(false) }
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val walletAddress = viewModel.walletAddress.collectAsState().value
    val currentUsername = userInfo?.username ?: viewModel.username.value ?: ""

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
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = username,
                            style = MaterialTheme.typography.labelLarge,
                            color = BonkOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if(!isOwnProfile) {
                CustomButton(
                    enabled = (walletAddress != null),
                    onClick = { setShowBoostModal(true) },
                    icon = Icons.Filled.RocketLaunch,
                    backgroundColor = BonkOrange,
                    text = if (walletAddress != null) stringResource(R.string.boost) else stringResource(R.string.no_wallet),
                    contentColor = BonkWhite,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(value = totalBoosts.toString(), label = stringResource(R.string.total_boosts), Modifier.weight(1f))
                StatCard(value = formatBonkEarned(totalBonkEarned), label = stringResource(R.string.total_bonk_earned), Modifier.weight(1f))
            }
        }
    }

    if (showBoostModal) {
        BoostBonkModal(
            onDismiss = { setShowBoostModal(false) },
            coroutineScope = coroutineScope,
            isLoading = isLoading,
            onSubmit = { amount ->
                setIsLoading(true)
                coroutineScope.launch {
                    val (success, json) = sendBonkFunctionRequest(
                        walletAddress ?: "",
                        amount
                    )
                    if (success && json?.has("tx") == true) {
                        val base64Tx = json.getString("tx")
                        val decodedTx = Base64.decode(base64Tx, Base64.DEFAULT)

                        val signResult = walletAdapter.transact(sender) {
                            signAndSendTransactions(arrayOf(decodedTx))
                        }

                        when (signResult) {
                            is TransactionResult.Success -> {
                                val sig = signResult.successPayload?.signatures?.firstOrNull()
                                val signatureBase58 = sig?.let { Base58.encodeToString(it) }
                                Log.d("SEND_BONK", "TX Signature: $signatureBase58")

                                viewModel.submitBoost(
                                    bonks = amount,
                                    postId = null,
                                    fromUserId = fromUserId,
                                    toUserId = toUserId
                                ) { success ->
                                    setIsLoading(false)
                                    setShowBoostModal(false)
                                    if (success) {
                                        viewModel.loadPostsByUsername(currentUsername)
                                        viewModel.loadUserStatsByUsername(currentUsername)
                                    }
                                }
                            }
                            is TransactionResult.NoWalletFound -> {
                                Log.e("SEND_BONK", "No wallet found.")
                                setIsLoading(false)
                            }
                            is TransactionResult.Failure -> {
                                Log.e("SEND_BONK", "Failed to sign/send: ${signResult.e.message}")
                                setIsLoading(false)
                            }
                        }
                    } else {
                        Log.e("SEND_BONK", "Failed to fetch transaction from server.")
                        setIsLoading(false)
                    }
                }
            }
        )
    }
}