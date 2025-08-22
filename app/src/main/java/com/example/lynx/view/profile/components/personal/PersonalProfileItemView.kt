package com.example.lynx.view.profile.components.personal

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun PersonalProfileItemView(
    navController: NavHostController,
    uuid: String,
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel
){
    val user = userViewModel.GetUserByUuid(uuid)
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profileViewModel.avatarCheck(user),
                contentDescription = "avatar",
                modifier = Modifier.size(60.dp).clip(CircleShape).clickable{
                    navController.navigate("avatarPreview/${user?.uuid}")
                },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user?.userName ?: "unknown",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.white)
                )
            }

            Image(
                painter = painterResource(R.drawable.ic_message),
                contentDescription = null,
                modifier = Modifier.clickable { navController.navigate("PersonalChatScreen/$uuid") }
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = user?.bio ?: "unknown",
            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(R.color.neon_green),
            modifier = Modifier.padding(start = 32.dp, end = 32.dp)
        )

    }
}