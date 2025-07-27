package com.example.boostbonk.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BoostBonkColorScheme = lightColorScheme(
    primary = BonkOrange,
    secondary = BonkYellow,
    tertiary = BonkBrown,
    background = BonkWhite,
    surface = BonkWhite,
    onPrimary = BonkWhite,
    onSecondary = BonkBlack,
    onTertiary = BonkWhite,
    onBackground = BonkBlack,
    onSurface = BonkBlack,
    error = BonkRed,
    onError = BonkWhite
)

@Composable
fun BoostBonkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BoostBonkColorScheme,
        typography = Typography,
        content = content
    )
}