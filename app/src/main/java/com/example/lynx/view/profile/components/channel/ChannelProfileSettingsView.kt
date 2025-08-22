package com.example.lynx.view.profile.components.channel

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun ChannelProfileSettingsView(
    navController: NavHostController,
    channelUuid: String,
    channelViewModel: ChannelViewModel,
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel
) {
    var userId = SessionManager.currentUserId.toString()
    val channel = channelViewModel.getChannelByUUID(channelUuid)
    Log.d("TAG", "колво участников - ${channel?.memberIds?.size}")
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
                .clickable {},
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_account),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Channel Id: ${channel?.id}",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorResource(R.color.white)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_notification),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Notification:",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.white)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Yes",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.white)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
                .clickable {
                    navController.navigate("MainScreen")
                    channelViewModel.exitChannel(
                        userId = SessionManager.currentUserId.toString(),
                        channel = channelViewModel.getChannelByUUID(channelUuid)!!
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_exit),
                contentDescription = "left group",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(21.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Left group",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.white)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            if(channelViewModel.isOwner(
                    userId = userId,
                    channel = channel
                )) {
                Text(
                    text = "Add member",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.neon_green),
                    modifier = Modifier.clickable {
                        navController.navigate("AddMemberChannel/${channel?.uuid}")
                    }
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Members:",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.white)
            )

            Spacer(modifier = Modifier.height(8.dp))
            channel?.memberIds?.forEach { uuid ->
                val user = userViewModel.GetUserByUuid(uuid)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable{navController.navigate("PersonalProfileScreen/${uuid}")},
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = profileViewModel.avatarCheck(user),
                        contentDescription = "avatar",
                        modifier = Modifier.run {
                            size(30.dp)
                                .clip(CircleShape)
                        },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = user?.userName ?: "empty",
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontSize = 16.sp,
                        color = colorResource(R.color.white)
                    )
                }
            }
        }
    }
}