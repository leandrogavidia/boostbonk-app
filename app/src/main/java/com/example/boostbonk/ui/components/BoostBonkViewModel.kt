package com.example.boostbonk

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boostbonk.data.model.Post
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BoostBonkViewModel : ViewModel() {

    private val supabase = SupabaseClientProvider.client

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _username = MutableStateFlow<String?>(null)
    val username: StateFlow<String?> = _username

    private val _fullName = MutableStateFlow<String?>(null)
    val fullName: StateFlow<String?> = _fullName

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email

    private val _avatarUrl = MutableStateFlow<String?>(null)
    val avatarUrl: StateFlow<String?> = _avatarUrl

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            val session = supabase.auth.currentSessionOrNull()

            if (session != null) {
                val data = session.user?.userMetadata
                Log.e("SessionCheck", data?.get("preferred_username").toString())
                Log.e("SessionCheck", session.toString())
                Log.e("SessionCheck", session.user?.id.toString())

                _isLoggedIn.value = true

                _avatarUrl.value = data?.get("avatar_url")?.toString()?.trim('"')
                _email.value = data?.get("email")?.toString()?.trim('"')
                _fullName.value = data?.get("full_name")?.toString()?.trim('"')
                _username.value = data?.get("preferred_username")?.toString()?.trim('"')
                _userId.value = session.user?.id

                Log.d("SessionCheck", "✅ Logged in as: ${_username.value}")
            } else {
                Log.d("SessionCheck", "🔒 No session found.")
            }
        }
    }

    fun getImageBytesFromUri(context: android.content.Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }

    fun submitPost(
        context: Context,
        description: String,
        imageUri: Uri?,
        username: String,
        userId: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                var imageUrl: String? = null

                if (imageUri != null) {
                    val imageBytes = getImageBytesFromUri(context, imageUri)
                    val filename = "post_${System.currentTimeMillis()}.jpg"

                    supabase.storage.from("images").upload(
                        path = filename,
                        data = imageBytes
                    ) {
                        upsert = true
                    }

                    imageUrl = supabase.storage.from("images").publicUrl(filename)
                }

                supabase.from("posts").insert(
                    Post(
                        username = username,
                        description = description,
                        user_id = userId,
                        image = imageUrl
                    )
                )

                onComplete(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }
}
