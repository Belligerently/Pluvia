package com.OxGames.Pluvia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alorma.compose.settings.ui.base.internal.SettingsTileColors
import com.alorma.compose.settings.ui.base.internal.SettingsTileDefaults

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

/* Friend Status Colors */
val friendAwayOrSnooze = Color(0x806DCFF6)
val friendInGame = Color(0xFF90BA3C)
val friendInGameAwayOrSnooze = Color(0x8090BA3C)
val friendOffline = Color(0xFF7A7A7A)
val friendOnline = Color(0xFF6DCFF6)

/**
 * Alorma compose settings tile colors
 */
@Composable
fun settingsTileColors(): SettingsTileColors = SettingsTileDefaults.colors(
    titleColor = MaterialTheme.colorScheme.onSurface,
    subtitleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f),
    actionColor = MaterialTheme.colorScheme.onSurface,
)

@Composable
fun settingsTileColorsAlt(): SettingsTileColors = SettingsTileDefaults.colors(
    titleColor = MaterialTheme.colorScheme.onSurface,
    subtitleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f),
)
