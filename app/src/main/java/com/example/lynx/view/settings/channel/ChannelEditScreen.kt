package com.example.lynx.view.settings.channel

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.lynx.view.settings.channel.components.ChannelAvatarEditView
import com.example.lynx.view.settings.channel.components.ChannelEditInfoView
import com.example.lynx.view.settings.channel.components.EditUsersForChannel
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun ChannelEditScreen(
    navController: NavHostController,
    channelUuid: String,
    channelViewModel: ChannelViewModel,
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel
) {
    val channel = channelViewModel.getChannelByUUID(channelUuid)
    var users = channelViewModel.getUsersByChannel(channel) ?: emptyList()

    var inputname by rememberSaveable { mutableStateOf("") }
    var inputId by rememberSaveable { mutableStateOf("") }

    val nameToSave = if (inputname.isBlank()) channel?.name ?: "" else inputname
    val idToSave = if (inputId.isBlank()) channel?.id ?: "" else inputId

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
                                navController.navigate("ChannelProfileScreen/$channelUuid")
                                channelViewModel.reset()
                            },
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Edit Channel",
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
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
                                channelViewModel.ApplyChannelEdit(
                                    id = idToSave,
                                    name = nameToSave,
                                    channelUUID = channelUuid
                                )
                                navController.navigate("ChannelProfileScreen/$channelUuid")
                            },
                        alignment = Alignment.TopEnd,
                        contentScale = ContentScale.Fit
                    )
                }
                ChannelAvatarEditView(
                    navController,
                    channelUuid,
                    channelViewModel = channelViewModel,
                    profileViewModel = profileViewModel,
                    inputName = inputname,
                    onNameChange = { inputname = it })

                Spacer(modifier = Modifier.height(10.dp))

                ChannelEditInfoView(
                    navController,
                    channelUuid = channelUuid,
                    channelViewModel = channelViewModel,
                    inputId = inputId,
                    onIdChange = { inputId = it })

                Spacer(modifier = Modifier.height(39.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text(
                        text = "Add member",
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(R.color.neon_green),
                        modifier = Modifier.clickable{
                            channel?.let {
                                navController.navigate("AddMemberChannel/${it.uuid}")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Members:",
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = colorResource(R.color.white)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn() {
                        items(users) { userId ->
                            val user = userViewModel.GetUserByUuid(userId)
                            if (user != null) {
                                EditUsersForChannel(
                                    navController = navController,
                                    channel = channel!!,
                                    user = user,
                                    profileViewModel = profileViewModel,
                                    channelViewModel = channelViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

