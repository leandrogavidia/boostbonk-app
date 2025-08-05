package com.example.boostbonk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostWithUser(
    val id: Long,
    val description: String,
    val image: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("user_id")
    val userId: String,
    val boosts: Int? = null,
    @SerialName("bonk_earned")
    val bonkEarned: Double? = null,
    val user: UserInfo? = null,
    val username: String,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    val email: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("wallet_address")
    val walletAddress: String? = null,
)
