package com.example.lynx.view.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lynx.R
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.repository.MessagesRepository.messages
import com.example.lynx.session.SessionManager
import com.example.lynx.view.chat.components.MessageBubbleView
import com.example.lynx.view.chat.components.MessageInputView
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.MessageViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun ChannelChatScreen(
    channelUuid: String,
    navController: NavController,
    userViewModel: UserViewModel,
    channelViewModel : ChannelViewModel,
    messageViewModel: MessageViewModel,
    profileViewModel: ProfileViewModel
){
    val channel = channelViewModel.getChannelByUUID(channelUuid)
    val inputText = remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val messageList = messageViewModel.getMessages(
        channelUuid = channel?.uuid ?:"",
        currentUserId = SessionManager.currentUserId.toString()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.black))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.statusBarsPadding())
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp)
                        .clickable { navController.navigate("MainScreen") },
                    contentScale = ContentScale.Fit
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = profileViewModel.avatarCheck(channel),
                            contentDescription = "avatar",
                            modifier = Modifier
                                .size(35.dp)
                                .clickable { navController.navigate("ChannelProfileScreen/${channel?.uuid}") }
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = channel?.name ?: "",
                            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                            fontWeight = FontWeight.W600,
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.neon_green)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(45.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues (start = 8.dp, end = 8.dp, top = 8.dp, bottom = 72.dp)
            ) {
                items(messageList) { message ->
                    MessageBubbleView(
                        navController = navController,
                        message = message,
                        userViewModel = userViewModel,
                        isChannel = true,
                        channel = channel
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            MessageInputView(
                inputText,
                channel?.ownerId ?: "empty",
                isChannel = true,
                channel = channel,
                messageViewModel = messageViewModel
            )
        }
        LaunchedEffect(messages.size) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }
}