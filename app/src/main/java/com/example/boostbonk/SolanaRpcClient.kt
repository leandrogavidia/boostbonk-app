package com.example.boostbonk

import android.net.Uri
import com.example.boostbonk.networking.KtorHttpDriver
import com.solana.networking.Rpc20Driver

object SolanaRpcClient {
    private val endpoint = Uri.parse("https://api.devnet.solana.com")
    val client = Rpc20Driver(endpoint.toString(), KtorHttpDriver())
}
