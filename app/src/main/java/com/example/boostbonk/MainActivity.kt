package com.example.boostbonk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender

class MainActivity : ComponentActivity() {

    private val supabase = SupabaseClientProvider.client
    private lateinit var sender: ActivityResultSender


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sender = ActivityResultSender(this)
        enableEdgeToEdge()

        setContent {
            BoostBonkApp(
                supabase = supabase,
                sender = sender,
                lifecycleScope = lifecycleScope,
            )
        }
    }

}
