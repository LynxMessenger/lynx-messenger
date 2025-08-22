@file:OptIn(ExperimentalFoundationApi::class)

package com.example.lynx.view.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.lynx.view.main.components.ChannelItemView
import com.example.lynx.view.main.components.GroupItemView
import com.example.lynx.view.main.components.PersonalItemView
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.PersonalViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    personalViewModel: PersonalViewModel,
    profileViewModel: ProfileViewModel,
    groupViewModel: GroupViewModel,
    channelViewModel: ChannelViewModel,
) {
    val user = userViewModel.GetUserByUuid(SessionManager.currentUserId.toString())

    val coroutineScope = rememberCoroutineScope()
    val tabTitles = listOf("Personal", "Group", "Channel")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })

    val personalChats = personalViewModel.getSortedChat(SessionManager.currentUserId.toString())
    val groupChats = user?.let { groupViewModel.getGroups(it) } ?: emptyList()
    val channelChats = user?.let { channelViewModel.getSortedChannel(it) } ?: emptyList()

    Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.black))) {
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
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp)
                        .clickable { navController.navigate("SettingScreen") },
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Messages",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.neon_green),
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(20.dp)
                        .clickable { navController.navigate("SearchScreen") },
                    contentScale = ContentScale.Fit
                )
            }

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = colorResource(id = R.color.neon_green),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .height(2.dp),
                        color = colorResource(id = R.color.neon_green)
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                fontSize = 18.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (pagerState.currentPage == index)
                                    colorResource(R.color.neon_green)
                                else
                                    colorResource(R.color.gray_light)
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(personalChats) { chat ->
                            PersonalItemView(
                                chat = chat,
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }
                    }

                    1 -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(groupChats) { groupChat ->
                            GroupItemView(
                                group = groupChat,
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }
                    }

                    2 -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(channelChats) { channelChat ->
                            ChannelItemView(
                                channel = channelChat,
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp, bottom = 20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(R.drawable.ic_create),
                contentDescription = "Create channel/group",
                modifier = Modifier
                    .size(56.dp)
                    .clickable { navController.navigate("NewMessagesScreen") }
            )
        }
    }
}
