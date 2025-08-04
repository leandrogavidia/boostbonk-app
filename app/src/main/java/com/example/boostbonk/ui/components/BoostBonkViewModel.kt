package com.example.boostbonk

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boostbonk.data.model.Boost
import com.example.boostbonk.data.model.WeeklyBoostRanking
import com.example.boostbonk.data.model.Post
import com.example.boostbonk.data.model.PostWithUser
import com.example.boostbonk.data.model.UserInfo
import com.example.boostbonk.data.model.WeeklyBonkEarnedRanking
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

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

    private val _posts = MutableStateFlow<List<PostWithUser>>(emptyList())
    val posts: StateFlow<List<PostWithUser>> = _posts

    private val _selectedUser = MutableStateFlow<UserInfo?>(null)
    val selectedUser: StateFlow<UserInfo?> = _selectedUser

    private val _userPosts = MutableStateFlow<List<PostWithUser>>(emptyList())
    val userPosts: StateFlow<List<PostWithUser>> = _userPosts

    private val _weeklyBoostRanking = MutableStateFlow<List<WeeklyBoostRanking>>(emptyList())
    val weeklyBoostRanking: StateFlow<List<WeeklyBoostRanking>> = _weeklyBoostRanking

    private val _weeklyBonkEarnedRanking = MutableStateFlow<List<WeeklyBonkEarnedRanking>>(emptyList())
    val weeklyBonkEarnedRanking: StateFlow<List<WeeklyBonkEarnedRanking>> = _weeklyBonkEarnedRanking

    val walletAddress = MutableStateFlow<String?>(null)
    val walletAddressPublic: StateFlow<String?> = walletAddress

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            val session = supabase.auth.currentSessionOrNull()
            val user = session?.user
            val metadata = user?.userMetadata

            if (session != null && user != null && metadata != null) {
                val userId = user.id
                val avatarUrl = metadata["avatar_url"]?.toString()?.trim('"')
                val email = metadata["email"]?.toString()?.trim('"')
                val fullName = metadata["full_name"]?.toString()?.trim('"')
                val username = metadata["preferred_username"]?.toString()?.trim('"')

                _isLoggedIn.value = true
                _avatarUrl.value = avatarUrl
                _email.value = email
                _fullName.value = fullName
                _username.value = username
                _userId.value = userId

                Log.d("SessionCheck", "✅ Logged in as: $username")

                try {
                    supabase.from("users").upsert(
                        mapOf(
                            "id" to userId,
                            "username" to username,
                            "avatar_url" to avatarUrl,
                            "email" to email,
                            "full_name" to fullName
                        )
                    )
                    Log.d("SessionCheck", "✅ User upserted into 'users' table")
                } catch (e: Exception) {
                    Log.e("SessionCheck", "❌ Failed to upsert user: ${e.message}")
                }

            } else {
                Log.d("SessionCheck", "🔒 No session found.")
            }
        }
    }

    fun getImageBytesFromUri(context: android.content.Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }



    fun getAllPosts() {
        viewModelScope.launch {
            try {
                val result = supabase
                    .from("posts_with_user")
                    .select()
                    .decodeList<PostWithUser>()
                    .sortedByDescending { it.createdAt }

                _posts.value = result

            } catch (e: Exception) {
                Log.e("getAllPosts", "Failed to load posts", e)
            }
        }
    }

    fun submitPost(
        context: Context,
        description: String,
        imageUri: Uri?,
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
                    mapOf(
                        "description" to description,
                        "user_id" to userId,
                        "image" to imageUrl
                    )
                )

                onComplete(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }

    fun submitBoost(
        bonks: Double,
        postId: Long?,
        fromUserId: String,
        toUserId: String,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                supabase.from("boosts").insert(
                    Boost(
                        bonks = bonks,
                        postId = postId,
                        fromUser = fromUserId,
                        toUser = toUserId
                    )
                )

                if (postId != null) {
                    val post = supabase
                        .from("posts")
                        .select {
                            filter {
                                eq("id", postId)
                            }
                        }
                        .decodeSingle<Post>()

                    val currentBoosts = post.boosts
                    val currentBonk = post.bonkEarned

                    val updateData = buildJsonObject {
                        put("boosts", JsonPrimitive(currentBoosts + 1))
                        put("bonk_earned", JsonPrimitive(currentBonk + bonks))
                    }

                    supabase.from("posts").update(updateData) {
                        filter {
                            eq("id", postId)
                        }
                    }
                }

                onComplete(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onComplete(false)
            }
        }
    }


    fun loadUserProfileByUsername(username: String?) {
        if (username == null) return

        viewModelScope.launch {
            try {
                val user = supabase
                    .from("users")
                    .select {
                        filter {
                            eq("username", username)
                        }
                    }
                    .decodeSingle<UserInfo>()

                _selectedUser.value = user
            } catch (e: Exception) {
                Log.e("loadUserProfile", "Failed to load user", e)
            }
        }
    }

    fun loadPostsByUsername(username: String) {
        viewModelScope.launch {
            try {
                val result = supabase
                    .from("posts_with_user")
                    .select {
                        filter {
                            eq("username", username)
                        }
                    }
                    .decodeList<PostWithUser>()
                    .sortedByDescending { it.createdAt }
                _userPosts.value = result
            } catch (e: Exception) {
                Log.e("loadPostsByUsername", "Failed to load user posts", e)
            }
        }
    }

    fun loadWeeklyBoostRanking() {
        viewModelScope.launch {
            try {
                val result = supabase
                    .from("weekly_boost_ranking")
                    .select()
                    .decodeList<WeeklyBoostRanking>()

                _weeklyBoostRanking.value = result
            } catch (e: Exception) {
                Log.e("Ranking", "Failed to fetch weekly boost ranking", e)
            }
        }
    }

    fun loadWeeklyBonkEarnedRanking() {
        viewModelScope.launch {
            try {
                val result = supabase
                    .from("weekly_bonk_earned_ranking")
                    .select()
                    .decodeList<WeeklyBonkEarnedRanking>()

                _weeklyBonkEarnedRanking.value = result
            } catch (e: Exception) {
                Log.e("Ranking", "Failed to fetch weekly bonk earned ranking", e)
            }
        }
    }


}


