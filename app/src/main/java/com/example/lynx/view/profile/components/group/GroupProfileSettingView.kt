package com.example.lynx.view.profile.components.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
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
import com.example.lynx.viewmodel.UserViewModel
import com.google.android.material.bottomappbar.BottomAppBar

@Composable
fun GroupProfileSettingsView(navController: NavHostController, groupUuid: String, groupViewModel: GroupViewModel, userViewModel: UserViewModel,profileViewModel: ProfileViewModel) {
    val group = groupViewModel.getGroupByUUID(groupUuid)
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
                    text = "Group Id: ${group?.id}",
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
                contentDescription = "notification",
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
                    groupViewModel.exitGroup(
                        userId = SessionManager.currentUserId.toString(),
                        group = groupViewModel.getGroupByUUID(groupUuid)!!
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
            if(groupViewModel.isOwner(userId = SessionManager.currentUserId.toString(), group = group)) {
                Text(
                    text = "Add member",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.neon_green),
                    modifier = Modifier.clickable {
                        navController.navigate("AddMemberGroup/${group?.uuid}")
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

            group?.memberIds?.forEach { uuid ->
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
