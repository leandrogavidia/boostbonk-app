package com.example.boostbonk.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoostRanking(
    val username: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("boost_count")
    val boostCount: Int

)
