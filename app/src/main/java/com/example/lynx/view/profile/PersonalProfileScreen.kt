package com.example.lynx.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.view.profile.components.personal.PersonalProfileItemView
import com.example.lynx.view.profile.components.personal.PersonalProfileSettingsView
import com.example.lynx.viewmodel.BlockViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun PersonalProfileScreen(
    navController: NavHostController,
    uuid: String,
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel,
    blockViewModel: BlockViewModel
){
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_backarrow),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp)
                            .clickable { navController.navigate("PersonalChatScreen/$uuid") },
                        contentScale = ContentScale.Fit
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        PersonalProfileItemView(
                            navController =  navController,
                            uuid =  uuid,
                            userViewModel = userViewModel,
                            profileViewModel = profileViewModel
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    item {
                        PersonalProfileSettingsView(
                            uuid,
                            userViewModel = userViewModel,
                            blockViewModel = blockViewModel,
                        )
                    }
                }
            }
        }
    }
}