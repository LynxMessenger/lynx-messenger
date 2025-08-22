package com.example.lynx.view.media

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun AvatarPreviewScreen(
    navController: NavHostController,
    uuid: String,
    userViewModel: UserViewModel,
    groupViewModel: GroupViewModel,
    channelViewModel: ChannelViewModel,
    profileViewModel: ProfileViewModel,
    isGroup: Boolean = false,
    isChannel: Boolean = false
) {
    val obj = when {
        isGroup -> groupViewModel.getGroupByUUID(uuid)
        isChannel -> channelViewModel.getChannelByUUID(uuid)
        else -> userViewModel.GetUserByUuid(uuid)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.black))
            .clickable { navController.popBackStack() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = profileViewModel.avatarCheck(obj),
            contentDescription = "Avatar Preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .border(2.dp, colorResource(R.color.neon_green), CircleShape)
        )
    }
}