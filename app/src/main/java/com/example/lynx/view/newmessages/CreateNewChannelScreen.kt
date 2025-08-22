package com.example.lynx.view.newmessages

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.view.newmessages.components.channel.AddUserForChannelScreen
import com.example.lynx.view.newmessages.components.channel.CreateChannelView
import com.example.lynx.view.newmessages.components.group.AddUserForGroupScreen
import com.example.lynx.view.newmessages.components.group.CreateGroupView
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.PersonalViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun CreateNewChannelScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    channelViewModel: ChannelViewModel,
    profileViewModel: ProfileViewModel,
    personalViewModel: PersonalViewModel
) {
    val users = personalViewModel.getSortedChat(SessionManager.currentUserId.toString())

    var currentUserId = SessionManager.currentUserId
    val name = channelViewModel.channelName.value
    val id = channelViewModel.channelId.value


    Box(modifier = Modifier.fillMaxSize()) {
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
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_backarrow),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp)
                            .clickable {
                                navController.navigate("NewMessagesScreen")
                                channelViewModel.reset()},
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "New Channel",
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        maxLines = 1,
                        color = colorResource(id = R.color.neon_green),
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_accept),
                        contentDescription = "Yes",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(20.dp)
                            .clickable {
                                if (id.isNotBlank() && name.isNotBlank()) {
                                    channelViewModel.createNewChannel(
                                        channelId = id,
                                        channelName = name,
                                        currentUserId = currentUserId ?: "empty"
                                    )
                                    navController.navigate("MainScreen")
                                }
                            },
                        alignment = Alignment.TopEnd,
                        contentScale = ContentScale.Fit
                    )
                }
                CreateChannelView(
                    navController = navController,
                    channelViewModel = channelViewModel,
                    inputName = channelViewModel.channelName.value,
                    onNameChange = { channelViewModel.channelName.value = it },
                    inputId = channelViewModel.channelId.value,
                    onIdChange = { channelViewModel.channelId.value = it },
                )

                Spacer(modifier = Modifier.padding(top = 12.dp))

                Image(
                    painter = painterResource(R.drawable.separation),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.padding(top = 12.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users) { user ->
                        AddUserForChannelScreen(
                            navController = navController,
                            user = user,
                            profileViewModel = profileViewModel,
                            userViewModel =  userViewModel,
                            channelViewModel = channelViewModel)
                        Spacer(modifier = Modifier.height(17.dp))
                    }
                }
            }
        }
    }
}