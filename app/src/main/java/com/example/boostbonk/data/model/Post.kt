package com.example.boostbonk.data.model
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val user_id: String,
    val username: String,
    val description: String,
    val image: String? = null,
    val boosts: Long = 0,
    val bonk_earned: Double = 0.0
)