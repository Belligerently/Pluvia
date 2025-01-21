package com.OxGames.Pluvia.ui.screen.friends

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.OxGames.Pluvia.data.FriendMessage
import com.OxGames.Pluvia.data.SteamFriend
import com.OxGames.Pluvia.db.dao.FriendMessagesDao
import com.OxGames.Pluvia.db.dao.SteamFriendDao
import com.OxGames.Pluvia.ui.component.topbar.BackButton
import com.OxGames.Pluvia.ui.theme.PluviaTheme
import com.OxGames.Pluvia.ui.util.ListItemImage
import com.OxGames.Pluvia.utils.getAvatarURL
import com.OxGames.Pluvia.utils.getProfileUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.dragonbra.javasteam.enums.EPersonaState
import `in`.dragonbra.javasteam.enums.EPersonaStateFlag
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChatState(
    val friend: SteamFriend = SteamFriend(0),
    val messages: List<FriendMessage> = listOf(),
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    friendDao: SteamFriendDao,
    messagesDao: FriendMessagesDao,
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()
}

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.chatState.collectAsStateWithLifecycle()

    ChatScreenContent(
        steamFriend = state.friend,
        messages = state.messages,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(
    steamFriend: SteamFriend,
    messages: List<FriendMessage>,
    onBack: () -> Unit,
) {
    val snackbarHost = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberLazyListState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ListItemImage(
                            image = { steamFriend.avatarHash.getAvatarURL() },
                            size = 40.dp,
                            contentDescription = "Logged in account user profile",
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Column {
                            CompositionLocalProvider(
                                LocalContentColor provides steamFriend.statusColor,
                                LocalTextStyle provides TextStyle(
                                    lineHeight = 1.em,
                                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                                ),
                            ) {
                                Text(
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 20.sp,
                                    maxLines = 1,
                                    text = buildAnnotatedString {
                                        append(steamFriend.nameOrNickname)
                                        if (steamFriend.statusIcon != null) {
                                            append(" ")
                                            appendInlineContent("icon", "[icon]")
                                        }
                                    },
                                    inlineContent = mapOf(
                                        "icon" to InlineTextContent(
                                            Placeholder(
                                                width = 16.sp,
                                                height = 16.sp,
                                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                                            ),
                                            children = {
                                                steamFriend.statusIcon?.let {
                                                    Icon(imageVector = it, tint = Color.LightGray, contentDescription = it.name)
                                                }
                                            },
                                        ),
                                    ),
                                )

                                Text(
                                    text = if (steamFriend.isPlayingGame) {
                                        // TODO get game names
                                        steamFriend.gameName.ifEmpty { "Playing game id: ${steamFriend.gameAppID}" }
                                    } else {
                                        steamFriend.state.name
                                    },
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    color = LocalContentColor.current.copy(alpha = .75f),
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    BackButton(onClick = onBack)
                },
                actions = {
                    Box {
                        IconButton(
                            onClick = { expanded = !expanded },
                            content = { Icon(imageVector = Icons.Default.MoreVert, contentDescription = null) },
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "View Profile") },
                                onClick = { uriHandler.openUri(steamFriend.id.getProfileUrl()) },
                            )
                            DropdownMenuItem(
                                text = { Text(text = "View Previous Names") },
                                onClick = { TODO() },
                            )
                            DropdownMenuItem(
                                text = { Text(text = "More Settings") },
                                onClick = {
                                    // TODO()
                                    //  3. Friend settings:
                                    //      3a. Add to favorites
                                    //      3b. Block communication
                                    //      3c. Friend (specific) notification settings
                                },
                            )
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        val sfd = remember {
            SimpleDateFormat("MMM d - h:mm a", Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }
        }

        // TODO Typing bar + Send + Emoji selector
        // TODO scroll to bottom
        // TODO scroll to bottom if we're ~3 messages slightly scrolled.
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding(),
            state = scrollState,
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(
                    message = msg.message,
                    timestamp = sfd.format(msg.timestamp * 1000L),
                    fromLocal = msg.fromLocal,
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun Preview_ChatScreenContent() {
    PluviaTheme {
        ChatScreenContent(
            steamFriend = SteamFriend(
                id = 76561198003805806,
                state = EPersonaState.Online,
                avatarHash = "cfc54391f2f2ba745b701ad1287f73e50dc26d74",
                name = "Lossy",
                nickname = "Lossy with a nickname which should clip",
                gameAppID = 440,
                stateFlags = EPersonaStateFlag.from(2048),
            ),
            messages = List(20) {
                FriendMessage(
                    id = it.plus(1).toLong(),
                    steamIDFriend = 76561198003805806,
                    fromLocal = it % 3 == 0,
                    message = "Hey!, ".repeat(it.plus(1).times(1)),
                    lowPriority = false,
                    timestamp = 1737438789,
                )
            },
            onBack = { },
        )
    }
}
