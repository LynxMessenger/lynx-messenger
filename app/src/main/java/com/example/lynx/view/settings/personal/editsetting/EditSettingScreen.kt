package com.example.lynx.view.settings.personal.editsetting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.view.settings.personal.editsetting.components.EditSettingView
import com.example.lynx.view.settings.personal.mainsettings.components.SettingsItemView
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun EditSettingScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel,
){
    val user = userViewModel.GetUserByUuid(uuid = SessionManager.currentUserId.toString())

    var inputNickname by rememberSaveable  { mutableStateOf("") }
    var inputBio by rememberSaveable  { mutableStateOf("") }

    val nicknameToSave = if (inputNickname.isBlank()) user?.userName ?: "" else inputNickname
    val bioToSave = if (inputBio.isBlank()) user?.bio ?: "" else inputBio


    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.black))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.statusBarsPadding())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_backarrow),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp)
                            .clickable { navController.navigate("SettingScreen")
                                userViewModel.selectedAvatarUri = null},
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Edit",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 36.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.neon_green),
                    )
                    Spacer(Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_accept),
                        contentDescription = "Yes",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(20.dp)
                            .clickable {
                                userViewModel.ApplyUserEdit(
                                    nickname = nicknameToSave,
                                    bio = bioToSave,
                                    currentUserId = SessionManager.currentUserId.toString()
                                )
                                navController.navigate("SettingScreen")
                            },
                        alignment = Alignment.TopEnd,
                        contentScale = ContentScale.Fit
                    )
                }
                EditSettingView(
                    navController = navController,
                    inputNickname = inputNickname,
                    onNicknameChange = { inputNickname = it },
                    inputBio = inputBio,
                    onBioChange = { inputBio = it },
                    profileViewModel = profileViewModel,
                    userViewModel = userViewModel
                )
                Spacer(modifier = Modifier.height(20.dp))
                SettingsItemView(navController)
            }
        }
    }
}
