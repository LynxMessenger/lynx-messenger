package com.example.lynx.view.settings.channel.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lynx.R
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.ProfileViewModel

@Composable
fun ChannelAvatarEditView(
    navController: NavHostController,
    channeluuid: String,
    channelViewModel : ChannelViewModel,
    profileViewModel: ProfileViewModel,
    inputName: String,
    onNameChange: (String) -> Unit,){

    val channel = channelViewModel.getChannelByUUID(channeluuid)
    val avatarUri = channelViewModel.selectedAvatarUri

    val avatarPainter = if(avatarUri != null){
        rememberAsyncImagePainter(avatarUri)
    }
    else{
        profileViewModel.avatarCheck(channel)
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = avatarPainter,
                contentDescription = "avatar",
                modifier = Modifier.size(60.dp).clip(CircleShape).clickable{
                    navController.navigate("avatarPickerChannel")
                },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = inputName,
                    onValueChange = onNameChange,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 24.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (inputName.isEmpty()) {
                                Text(
                                    text = channel?.name ?: "unknown",
                                    style = TextStyle(
                                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}