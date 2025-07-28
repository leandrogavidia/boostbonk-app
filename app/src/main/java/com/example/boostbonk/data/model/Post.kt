package com.example.boostbonk.data.model

data class Post(
    val username: String,
    val description: String,
    val time: String,
    val totalBoosts: Int,
    val totalBonks: Double
)