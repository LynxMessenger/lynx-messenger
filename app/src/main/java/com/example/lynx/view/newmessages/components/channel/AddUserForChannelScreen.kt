package com.example.lynx.view.newmessages.components.channel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.example.lynx.model.Chat
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun AddUserForChannelScreen(
    navController: NavController,
    user: Chat,
    profileViewModel: ProfileViewModel,
    userViewModel: UserViewModel,
    channelViewModel: ChannelViewModel
) {
    val user = userViewModel.GetUserByUuid(user.user.uuid)
    val userId = user?.uuid ?: return

    val isAlreadySelected = remember {
        derivedStateOf { userId in channelViewModel.selectedMembers }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable(enabled = !isAlreadySelected.value) {
                    channelViewModel.addMemberInNewChannel(userId)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profileViewModel.avatarCheck(user),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.userName,
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = colorResource(R.color.white),
                )
            }

            AnimatedVisibility(
                visible = !isAlreadySelected.value,
                exit = fadeOut() + scaleOut(),
                enter = fadeIn() + scaleIn()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_accept),
                    contentDescription = "Accept",
                    tint = colorResource(R.color.neon_green),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )
            }
        }
    }
}
