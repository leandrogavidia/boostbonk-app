package com.example.boostbonk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyStatsSummary(
    @SerialName("weekly_total_boosts")
    val weeklyTotalBoosts: Int,
    @SerialName("weekly_total_bonk_earned")
    val weeklyTotalBonkEarned: Int
)