package com.example.lynx.view.profile.components.group

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
import com.example.lynx.session.SessionManager
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.ProfileViewModel

@Composable
fun GroupProfileView(navController: NavHostController, groupUuid: String, groupViewModel : GroupViewModel, profileViewModel: ProfileViewModel){
    val group = groupViewModel.getGroupByUUID(groupUuid)
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = profileViewModel.avatarCheck(group),
                contentDescription = "avatar",
                modifier = Modifier.size(60.dp).clip(CircleShape).clickable{
                    navController.navigate("avatarPreview/${group?.uuid}?type=group")
                },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = group?.name ?: "unknown",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.white)
                )
            }

            if(groupViewModel.isOwner(userId = SessionManager.currentUserId.toString(), group = group)){
                Image(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    modifier = Modifier.clickable { navController.navigate("GroupEditScreen/${groupUuid}") }
                )
            }
        }
    }
}