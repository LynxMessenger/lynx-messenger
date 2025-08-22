package com.example.lynx.view.settings.personal.accountsetting

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.example.lynx.repository.UserRepository
import com.example.lynx.session.SessionManager
import com.example.lynx.view.settings.personal.accountsetting.components.AccountSettingView
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun AccountSettingScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    ){
    val user = UserRepository.users.find { it.uuid == SessionManager.currentUserId }

    var inputId by remember { mutableStateOf("") }
    val idToSave = if (inputId.isBlank()) user?.id ?: "" else inputId

    var inputPassword by remember { mutableStateOf("") }
    val passwordToSave = if (inputPassword.isBlank()) user?.password ?: "" else inputPassword

    var inputEmail by remember { mutableStateOf("") }
    val emailToSave = if (inputEmail.isBlank()) user?.email ?: "" else inputEmail
    LocalContext.current

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
                            .clickable { navController.navigate("SettingScreen") },
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Account",
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
                                userViewModel.ApplyUserAccountEdit(
                                    password = passwordToSave,
                                    id = idToSave,
                                    email = emailToSave,
                                    currentUserId = SessionManager.currentUserId.toString()
                                )
                                navController.navigate("SettingScreen")
                            },
                        alignment = Alignment.TopEnd,
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(65.dp))
                AccountSettingView(
                    inputId = inputId,
                    onIdChange = { inputId = it },
                    inputPassword = inputPassword,
                    onPasswordChange = { inputPassword = it },
                    inputEmail = inputEmail,
                    onEmailChange = { inputEmail = it },
                    userViewModel = userViewModel,
                )
            }
        }
    }
}