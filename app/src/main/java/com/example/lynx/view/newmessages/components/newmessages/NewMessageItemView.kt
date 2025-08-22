package com.example.lynx.view.newmessages.components.newmessages

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.model.Chat
import com.example.lynx.viewmodel.ProfileViewModel

@Composable
fun NewMessageItemView(navController: NavHostController, chat: Chat, profileViewModel: ProfileViewModel) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .clickable { navController.navigate("PersonalChatScreen/${chat.user.uuid}") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profileViewModel.avatarCheck(chat.user),
                contentDescription = "avatar",
                modifier = Modifier.size(40.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = chat.user.userName,
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = colorResource(R.color.white),
                )
            }
        }
    }
}