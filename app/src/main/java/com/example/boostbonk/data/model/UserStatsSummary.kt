package com.example.boostbonk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserStatsSummary(
    val username: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("total_boosts")
    val totalBoosts: Int,
    @SerialName("total_bonk_earned")
    val totalBonkEarned: Double
)