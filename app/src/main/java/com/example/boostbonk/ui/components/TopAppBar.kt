package com.example.boostbonk.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkBlack
import com.example.boostbonk.ui.theme.BonkOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithWallet(
    isLoggedIn: Boolean,
    walletAddress: String?,
    onConnectWallet: () -> Unit
) {
    if (!isLoggedIn) return

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BonkBlack
        ),
        title = {},
        actions = {
            if (walletAddress != null) {
                Text(
                    text = walletAddress.take(4) + "..." + walletAddress.takeLast(4),
                    modifier = Modifier.padding(end = 16.dp),
                    color = BonkOrange
                )
            } else {
                TextButton(onClick = onConnectWallet) {
                    Text(
                        stringResource(R.string.connect_wallet),
                        color = BonkOrange
                    )
                }
            }
        }
    )
}
