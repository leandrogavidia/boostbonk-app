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
            supabaseUrl = "https://ekjjucdrpkqujyjhrjws.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVramp1Y2RycGtxdWp5amhyandzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4MTM0MzQsImV4cCI6MjA2OTM4OTQzNH0.QhYvfL__NInC44o0ApQW9FKUblAdFDH1WHVEGfAvc7I"
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
