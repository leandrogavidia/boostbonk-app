package com.example.boostbonk.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val username: String,
    @SerialName("full_name")
    val fullName: String? = null,
    val email: String? = null,
    @SerialName("avatar_url")
    val avatarUrl: String? = null
)