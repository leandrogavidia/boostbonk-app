package com.example.boostbonk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(kotlin.time.ExperimentalTime::class)
class DeepLinkHandlerActivity : ComponentActivity() {

    private val supabase = SupabaseClientProvider.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val originalUri = intent.dataString
                val uri = Uri.parse(originalUri)
                val code = uri.getQueryParameter("code")

                if (code != null) {
                    val session = supabase.auth.exchangeCodeForSession(code)
                    Log.d("SessionCheck", "✅ Session: ${session.user?.email}")

                    withContext(Dispatchers.Main) {
                        navigateToMainApp()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        navigateToMainApp()
                    }
                }
            } catch (e: Exception) {
                Log.e("SessionCheck", "💥 Deeplink error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    navigateToMainApp()
                }
            }
        }
    }

    private fun navigateToMainApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}
