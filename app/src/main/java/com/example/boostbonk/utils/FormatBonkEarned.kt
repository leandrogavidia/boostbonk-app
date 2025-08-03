package com.example.boostbonk.utils

import java.util.Locale

fun formatBonkEarned(value: Double): String {
    fun Double.formatSuffix(suffix: String, divisor: Double): String {
        val divided = this / divisor
        return if (divided % 1 == 0.0) "${divided.toInt()}$suffix"
        else String.format(Locale.US, "%.1f%s", divided, suffix)
    }

    return when {
        value >= 1_000_000_000 -> value.formatSuffix("B", 1_000_000_000.0)
        value >= 1_000_000     -> value.formatSuffix("M", 1_000_000.0)
        value >= 1_000         -> value.formatSuffix("K", 1_000.0)
        else -> value.toInt().toString()
    }
}
