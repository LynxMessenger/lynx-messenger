package com.example.lynx.view.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lynx.R
import com.example.lynx.model.Group
import com.example.lynx.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun GroupItemView(group: Group, navController: NavController, profileViewModel: ProfileViewModel) {

    val lastMessage = group.messages.lastOrNull()

    val inputFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val timeOnly = try {
        val date = inputFormat.parse(lastMessage?.timestamp)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        lastMessage?.timestamp
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("GroupChatScreen/${group.uuid}") },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profileViewModel.avatarCheck(group),
                contentDescription = "avatar",
                modifier = Modifier.size(50.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = group.name,
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorResource(R.color.white),
                    modifier = Modifier.padding(top = 20.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Column {
                    group.messages.takeLast(1).forEach { message ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when {
                                    message.imageUri != null -> "image"
                                    message.videoUri != null -> "video"
                                    message.audioUri != null -> "voice"
                                    else -> message.text.toString()
                                },
                                modifier = Modifier.widthIn(max = 175.dp),
                                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = colorResource(R.color.gray_light),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = timeOnly.toString(),
                                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                fontSize = 14.sp,
                                color = colorResource(R.color.gray_light)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
        Image(
            painter = painterResource(R.drawable.separation),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}
