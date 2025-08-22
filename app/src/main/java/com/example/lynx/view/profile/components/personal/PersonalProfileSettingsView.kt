package com.example.lynx.view.profile.components.personal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.viewmodel.BlockViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun PersonalProfileSettingsView(
    uuid: String,
    userViewModel: UserViewModel,
    blockViewModel: BlockViewModel,
){
    var currentUser = userViewModel.GetUserByUuid(SessionManager.currentUserId.toString())
    val user = userViewModel.GetUserByUuid(uuid)
    var isBlocked by remember { mutableStateOf(blockViewModel.isUserBlocked(uuid, currentUser = currentUser!!)) }
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
                    text = "User Id: ${user?.id}",
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
                contentDescription = null,
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
                    if (isBlocked) {
                        blockViewModel.unblockUser(uuid, currentUser = currentUser!!)
                    } else {
                        blockViewModel.blockUser(uuid, currentUser = currentUser!!)
                    }
                    isBlocked = !isBlocked
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_block),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Block:",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.white)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (isBlocked) "Yes" else "No",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.white)
            )
        }
    }
}
