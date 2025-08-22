package com.example.lynx.view.settings.personal.editsetting.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.lynx.session.SessionManager
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun EditSettingView(
    navController: NavHostController,
    inputNickname: String,
    onNicknameChange: (String) -> Unit,
    inputBio: String,
    onBioChange: (String) -> Unit,
    profileViewModel: ProfileViewModel,
    userViewModel: UserViewModel
) {
    val user = userViewModel.GetUserByUuid(SessionManager.currentUserId.toString())
    val avatarUri = userViewModel.selectedAvatarUri
    val avatarPainter = if (avatarUri != null) {
        rememberAsyncImagePainter(avatarUri)
    } else {
        profileViewModel.avatarCheck(user)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = avatarPainter,
                contentDescription = "avatar",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("avatarPicker")
                    },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                BasicTextField(
                    value = inputNickname,
                    onValueChange = onNicknameChange,
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
                            if (inputNickname.isEmpty()) {
                                Text(
                                    text = user?.userName ?: "unknown",
                                    style = TextStyle(
                                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user?.id ?: "unknown",
                    style = TextStyle(
                        color = colorResource(R.color.white),
                        fontSize = 12.sp
                    )
                )
            }
        }

        BasicTextField(
            value = inputBio,
            onValueChange = onBioChange,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(R.color.neon_green)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .heightIn(min = 24.dp),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (inputBio.isEmpty()) {
                        Text(
                            text = user?.bio ?: "unknown",
                            style = TextStyle(
                                color = colorResource(R.color.gray_light),
                                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                fontSize = 16.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}