package com.example.boostbonk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Boost(
    val id: Long? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null,
    val bonks: Double,
    @SerialName("post_id")
    val postId: Long? = null,
    @SerialName("from_user")
    val fromUser: String,
    @SerialName("to_user")
    val toUser: String
)
