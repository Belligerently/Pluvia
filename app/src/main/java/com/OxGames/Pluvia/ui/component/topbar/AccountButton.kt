package com.OxGames.Pluvia.ui.component.topbar

import android.content.res.Configuration
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.OxGames.Pluvia.PluviaApp
import com.OxGames.Pluvia.SteamService
import com.OxGames.Pluvia.data.SteamFriend
import com.OxGames.Pluvia.events.SteamEvent
import com.OxGames.Pluvia.ui.component.dialog.ProfileDialog
import com.OxGames.Pluvia.ui.theme.PluviaTheme
import com.OxGames.Pluvia.ui.util.ListItemImage
import `in`.dragonbra.javasteam.enums.EPersonaState

@Composable
fun AccountButton(
    contentDescription: String? = null,
) {
    var persona by remember {
        var persona: SteamFriend? = null
        SteamService.getUserSteamId()?.let { id ->
            persona = SteamService.getPersonaStateOf(id)
        }
        mutableStateOf(persona)
    }

    DisposableEffect(true) {
        val onPersonaStateReceived: (SteamEvent.PersonaStateReceived) -> Unit = { event ->
            event.persona?.let { persona = it }
        }

        PluviaApp.events.on<SteamEvent.PersonaStateReceived, Unit>(onPersonaStateReceived)

        onDispose {
            PluviaApp.events.off<SteamEvent.PersonaStateReceived, Unit>(onPersonaStateReceived)
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    ProfileDialog(
        openDialog = showDialog,
        name = persona?.name.orEmpty(),
        avatarHash = persona?.avatarHash.orEmpty(),
        state = EPersonaState.from(persona?.state ?: 0),
        onStatusChange = {
            // TODO status change
        },
        onSettings = {
            // TODO settings
            showDialog = false
        },
        onLogout = {
            // TODO logout
            showDialog = false
        },
        onDismiss = {
            showDialog = false
        },
    )

    IconButton(
        onClick = { showDialog = true },
        content = {
            ListItemImage(
                image = { SteamService.getAvatarURL(persona?.avatarHash.orEmpty()) },
                contentDescription = contentDescription,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview_AccountButton() {
    PluviaTheme {
        CenterAlignedTopAppBar(
            title = { Text("Top App Bar") },
            actions = { AccountButton() }
        )
    }
}