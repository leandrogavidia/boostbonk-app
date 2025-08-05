package com.example.boostbonk.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.boostbonk.R
import com.example.boostbonk.ui.theme.BonkOrange
import com.example.boostbonk.ui.theme.BonkWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletUpdateModal(
    onDismiss: () -> Unit,
    currentAddress: String?,
    onSubmit: (String) -> Unit,
    coroutineScope: CoroutineScope,
    isLoading: Boolean
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val (walletAddressInput, setWalletAddressInput) = remember { mutableStateOf(currentAddress ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BonkWhite
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = if (currentAddress.isNullOrBlank()) stringResource(R.string.set_wallet) else stringResource(
                    R.string.update_wallet
                ),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = walletAddressInput,
                onValueChange = setWalletAddressInput,
                label = { Text(stringResource(R.string.wallet_address)) },
                placeholder = { Text(stringResource(R.string.enter_wallet_address)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        onSubmit(walletAddressInput)
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = BonkOrange,
                    contentColor = BonkWhite
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = stringResource(R.string.confirm),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
