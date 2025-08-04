package com.example.boostbonk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllTimeStatsSummary(
    @SerialName("all_time_total_boosts")
    val allTimeTotalBoosts: Int,
    @SerialName("all_time_total_bonk_earned")
    val allTimeTotalBonkEarned: Double
)