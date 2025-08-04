package com.example.boostbonk.solana

import android.net.Uri
import com.solana.mobilewalletadapter.clientlib.*

val solanaUri = Uri.parse("https://boostbonk.com")
val iconUri = Uri.parse("favicon.ico")
val identityName = "BoostBonk"

val walletAdapter = MobileWalletAdapter(
    connectionIdentity = ConnectionIdentity(
        identityUri = solanaUri,
        iconUri = iconUri,
        identityName = identityName
    )
)