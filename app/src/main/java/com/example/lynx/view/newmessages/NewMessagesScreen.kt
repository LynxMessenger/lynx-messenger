package com.example.lynx.view.newmessages


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.view.newmessages.components.newmessages.NewChatsItemView
import com.example.lynx.view.newmessages.components.newmessages.NewMessageItemView
import com.example.lynx.viewmodel.PersonalViewModel
import com.example.lynx.viewmodel.ProfileViewModel

@Composable
fun NewMessagesScreen(
    navController: NavHostController,
    personalViewModel: PersonalViewModel,
    profileViewModel: ProfileViewModel,
){
    val chats = personalViewModel.getSortedChat(SessionManager.currentUserId.toString())

    Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.black))) {

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.statusBarsPadding())

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp)
                        .align(Alignment.CenterStart)
                        .clickable { navController.navigate("MainScreen") },
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "New Messages",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600,
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    color = colorResource(R.color.neon_green),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            NewChatsItemView(navController)
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Image(
                painter = painterResource(R.drawable.separation),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(chats) { chat ->
                    NewMessageItemView(
                        navController = navController,
                        chat = chat,
                        profileViewModel = profileViewModel)
                    Spacer(modifier = Modifier.height(17.dp))
                }
            }
        }
    }
}