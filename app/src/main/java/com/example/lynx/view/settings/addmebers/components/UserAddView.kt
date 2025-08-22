package com.example.lynx.view.settings.addmebers.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.model.Channel
import com.example.lynx.model.Group
import com.example.lynx.model.User
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.ProfileViewModel

@Composable
fun UserAddView(
    user: User,
    memberIds: List<String>,
    group: Group? = null,
    channel: Channel? = null,
    profileViewModel: ProfileViewModel,
    groupViewModel: GroupViewModel,
    channelViewModel: ChannelViewModel
) {

    val isAlreadyAdd = user.uuid in memberIds

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
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
            text = user.userName,
            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
            fontSize = 16.sp,
            color = colorResource(R.color.white)
        )

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = !isAlreadyAdd,
            exit = fadeOut() + scaleOut(),
            enter = fadeIn() + scaleIn()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_accept),
                contentDescription = "addMember",
                tint = colorResource(R.color.neon_green),
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        if(group != null) {
                            groupViewModel.addMemberByGroup(
                                group = group,
                                userId = user.uuid
                            )
                        }
                        if(channel != null)
                            channelViewModel.addMemberByChannel(
                                channelId = channel.uuid,
                                userId = user.uuid)
                    }
            )
        }
    }
}

