package com.example.boostbonk.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.boostbonk.SupabaseClientProvider
import com.example.boostbonk.data.model.AllTimeStatsSummary
import com.example.boostbonk.data.model.Boost
import com.example.boostbonk.data.model.Post
import com.example.boostbonk.data.model.PostWithUser
import com.example.boostbonk.data.model.UserInfo
import com.example.boostbonk.data.model.BonkEarnedRanking
import com.example.boostbonk.data.model.BoostRanking
import com.example.boostbonk.data.model.UserStatsSummary
import com.example.boostbonk.data.model.WeeklyStatsSummary
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

    private val _isLoadingPosts = MutableStateFlow(true)
    val isLoadingPosts: StateFlow<Boolean> = _isLoadingPosts

    private val _selectedUser = MutableStateFlow<UserInfo?>(null)
    val selectedUser: StateFlow<UserInfo?> = _selectedUser

    private val _userPosts = MutableStateFlow<List<PostWithUser>>(emptyList())
    val userPosts: StateFlow<List<PostWithUser>> = _userPosts

    private val _isLoadingUserPosts = MutableStateFlow(true)
    val isLoadingUserPosts: StateFlow<Boolean> = _isLoadingUserPosts

    private val _weeklyBoostRanking = MutableStateFlow<List<BoostRanking>>(emptyList())
    val weeklyBoostRanking: StateFlow<List<BoostRanking>> = _weeklyBoostRanking

    private val _weeklyBonkEarnedRanking =
        MutableStateFlow<List<BonkEarnedRanking>>(emptyList())
    val weeklyBonkEarnedRanking: StateFlow<List<BonkEarnedRanking>> = _weeklyBonkEarnedRanking

    private val _isLoadingWeeklyBonkEarnedRanking = MutableStateFlow(true)
    val isLoadingWeeklyBonkEarnedRanking: StateFlow<Boolean> = _isLoadingWeeklyBonkEarnedRanking

    private val _isLoadingWeeklyBoostRanking = MutableStateFlow(true)
    val isLoadingWeeklyBoostRanking: StateFlow<Boolean> = _isLoadingWeeklyBoostRanking

    private val _allTimeBonkEarnedRanking = MutableStateFlow<List<BonkEarnedRanking>>(emptyList())
    val allTimeBonkEarnedRanking: StateFlow<List<BonkEarnedRanking>> = _allTimeBonkEarnedRanking

    private val _isLoadingAllTimeBonkEarnedRanking = MutableStateFlow(true)
    val isLoadingAllTimeBonkEarnedRanking: StateFlow<Boolean> = _isLoadingAllTimeBonkEarnedRanking

    private val _allTimeBoostRanking = MutableStateFlow<List<BoostRanking>>(emptyList())
    val allTimeBoostRanking: StateFlow<List<BoostRanking>> = _allTimeBoostRanking

    private val _isLoadingAllTimeBoostRanking = MutableStateFlow(true)
    val isLoadingAllTimeBoostRanking: StateFlow<Boolean> = _isLoadingAllTimeBoostRanking

    val connectedWalletAddress = MutableStateFlow<String?>(null)
    val connectedWalletAddressPublic: StateFlow<String?> = connectedWalletAddress

    val userWalletAddress = MutableStateFlow<String?>(null)
    val userWalletAddressPublic: StateFlow<String?> = userWalletAddress

    private val _weeklyStats = MutableStateFlow<WeeklyStatsSummary?>(null)
    val weeklyStats: StateFlow<WeeklyStatsSummary?> = _weeklyStats

    private val _allTimeStats = MutableStateFlow<AllTimeStatsSummary?>(null)
    val allTimeStats: StateFlow<AllTimeStatsSummary?> = _allTimeStats

    private val _isLoadingWeeklyStats = MutableStateFlow(true)
    val isLoadingWeeklyStats: StateFlow<Boolean> = _isLoadingWeeklyStats

    private val _isLoadingAllTimeStats = MutableStateFlow(true)
    val isLoadingAllTimeStats: StateFlow<Boolean> = _isLoadingAllTimeStats

    private val _userStats = MutableStateFlow<UserStatsSummary?>(null)
    val userStats: StateFlow<UserStatsSummary?> = _userStats

    private val _isLoadingUserStats = MutableStateFlow(false)
    val isLoadingUserStats: StateFlow<Boolean> = _isLoadingUserStats

    init {
        loadSession()
        loadAllTimeStats()
        loadWeeklyStats()
        loadAllPosts()
    }

    fun loadSession() {
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

    fun getImageBytesFromUri(context: Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        return inputStream?.readBytes() ?: byteArrayOf()
    }

    fun loadAllPosts() {
        viewModelScope.launch {
            _isLoadingPosts.value = true
            try {
                val result = supabase
                    .from("posts_with_user")
                    .select()
                    .decodeList<PostWithUser>()
                    .sortedByDescending { it.createdAt }

                _posts.value = result
            } catch (e: Exception) {
                Log.e("getAllPosts", "Failed to load posts", e)
            } finally {
                _isLoadingPosts.value = false
            }
        }
    }
    fun submitPost(
        context: Context,
        description: String,
        imageUri: Uri?,
        onComplete: (Boolean) -> Unit
    ) {
        val userId = _userId.value
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
                Log.e("submitPost", "Failed to submit post", e)
                onComplete(false)
            }
        }
    }

    fun submitBoost(
        bonks: Double,
        postId: Long?,
        toUserId: String,
        onComplete: (Boolean) -> Unit
    ) {
        val fromUserId = _userId.value

        if (fromUserId == null) {
            return
        }

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
            _isLoadingUserPosts.value = true
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
            } finally {
                _isLoadingUserPosts.value = false
            }
        }
    }

    fun loadWeeklyBoostRanking() {
        viewModelScope.launch {
            _isLoadingWeeklyBoostRanking.value = true
            try {
                val result = supabase
                    .from("weekly_boost_ranking")
                    .select()
                    .decodeList<BoostRanking>()

                _weeklyBoostRanking.value = result
            } catch (e: Exception) {
                Log.e("Ranking", "Failed to fetch weekly boost ranking", e)
            } finally {
                _isLoadingWeeklyBoostRanking.value = false
            }
        }
    }

    fun loadWeeklyBonkEarnedRanking() {
        viewModelScope.launch {
            _isLoadingWeeklyBonkEarnedRanking.value = true
            try {
                val result = supabase
                    .from("weekly_bonk_earned_ranking")
                    .select()
                    .decodeList<BonkEarnedRanking>()

                _weeklyBonkEarnedRanking.value = result
            } catch (e: Exception) {
                Log.e("Ranking", "Failed to fetch weekly bonk earned ranking", e)
            } finally {
                _isLoadingWeeklyBonkEarnedRanking.value = false
            }
        }
    }

    fun loadAllTimeBonkEarnedRanking() {
        viewModelScope.launch {
            _isLoadingAllTimeBonkEarnedRanking.value = true
            try {
                val result = supabase
                    .from("all_time_bonk_earned_ranking")
                    .select()
                    .decodeList<BonkEarnedRanking>()

                _allTimeBonkEarnedRanking.value = result
            } catch (e: Exception) {
                Log.e("Ranking", "Failed to fetch all-time bonk earned ranking", e)
            } finally {
                _isLoadingAllTimeBonkEarnedRanking.value = false
            }
        }
    }

    fun loadAllTimeBoostRanking() {
        viewModelScope.launch {
            _isLoadingAllTimeBoostRanking.value = true
            try {
                val result = supabase
                    .from("all_time_boost_ranking")
                    .select()
                    .decodeList<BoostRanking>()

                _allTimeBoostRanking.value = result
            } catch (e: Exception) {
                Log.e("Ranking", "Failed to fetch all-time boost ranking", e)
            } finally {
                _isLoadingAllTimeBoostRanking.value = false
            }
        }
    }

    fun loadWeeklyStats() {
        viewModelScope.launch {
            _isLoadingWeeklyStats.value = true
            try {
                val result = supabase
                    .from("weekly_stats_summary")
                    .select()
                    .decodeSingle<WeeklyStatsSummary>()

                _weeklyStats.value = result
            } catch (e: Exception) {
                Log.e("Stats", "Failed to fetch weekly stats", e)
            } finally {
                _isLoadingWeeklyStats.value = false
            }
        }
    }

    fun loadAllTimeStats() {
        viewModelScope.launch {
            _isLoadingAllTimeStats.value = true
            try {
                val result = supabase
                    .from("all_time_stats_summary")
                    .select()
                    .decodeSingle<AllTimeStatsSummary>()

                _allTimeStats.value = result
            } catch (e: Exception) {
                Log.e("Stats", "Failed to fetch all-time stats", e)
            } finally {
                _isLoadingAllTimeStats.value = false
            }
        }
    }

    fun loadUserStatsByUsername(username: String?) {
        if (username == null) return

        viewModelScope.launch {
            _isLoadingUserStats.value = true
            try {
                val result = supabase
                    .from("user_stats_summary")
                    .select {
                        filter {
                            eq("username", username)
                        }
                    }
                    .decodeSingle<UserStatsSummary>()

                _userStats.value = result
            } catch (e: Exception) {
                Log.e("UserStats", "Failed to load user stats", e)
            } finally {
                _isLoadingUserStats.value = false
            }
        }
    }

    fun setUserWalletAddress(username: String, newWalletAddress: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                supabase.from("users").update(
                    mapOf("wallet_address" to newWalletAddress)
                ) {
                    filter {
                        eq("username", username)
                    }
                }

                userWalletAddress.value = newWalletAddress
                onComplete(true)
            } catch (e: Exception) {
                Log.e("WalletUpdate", "Failed to update wallet address", e)
                onComplete(false)
            }
        }
    }
}