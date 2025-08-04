package com.example.boostbonk.solana

import android.util.Base64
import android.util.Log
import com.funkatronics.encoders.Base58
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.MobileWalletAdapter
import com.solana.mobilewalletadapter.clientlib.TransactionResult
import com.solana.mobilewalletadapter.clientlib.successPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun signAndSendSerializedTx(
    sender: ActivityResultSender,
    walletAdapter: MobileWalletAdapter,
    base64Tx: String,
    coroutineScope: CoroutineScope,
    onResult: (Boolean, String?) -> Unit
) {
    coroutineScope.launch {
        val decodedTx: ByteArray = try {
            Base64.decode(base64Tx, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e("MWA", "Failed to decode base64 transaction: ${e.message}")
            onResult(false, null)
            return@launch
        }

        val result = walletAdapter.transact(sender) {
            signAndSendTransactions(arrayOf(decodedTx))
        }

        when (result) {
            is TransactionResult.Success -> {
                val sig = result.successPayload?.signatures?.firstOrNull()
                val signatureBase58 = sig?.let { Base58.encodeToString(it) }
                Log.d("MWA", "Transaction sent successfully: $signatureBase58")
                onResult(true, signatureBase58)
            }

            is TransactionResult.NoWalletFound -> {
                Log.e("MWA", "No wallet found")
                onResult(false, null)
            }

            is TransactionResult.Failure -> {
                Log.e("MWA", "Transaction failed: ${result.e.message}")
                onResult(false, null)
            }
        }
    }
}
