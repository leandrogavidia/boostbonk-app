// SupabaseClientProvider.kt
package com.example.boostbonk

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseClientProvider {

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "",
            supabaseKey = ""
        ) {
            install(Postgrest)
            install(Auth) {
                autoSaveToStorage = true
                autoLoadFromStorage = true
                flowType = FlowType.PKCE
                scheme = "boostbonk"
                host = "auth"
            }
            install(Storage)
        }
    }
}
