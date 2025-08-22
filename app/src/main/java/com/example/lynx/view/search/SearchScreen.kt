package com.example.lynx.view.search

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.model.SearchResult
import com.example.lynx.session.SessionManager
import com.example.lynx.view.search.components.ChannelFindView
import com.example.lynx.view.search.components.GroupFindView
import com.example.lynx.view.search.components.UserFindView
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.PersonalViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.SearchViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    personalViewModel: PersonalViewModel,
    groupViewModel: GroupViewModel,
    channelViewModel: ChannelViewModel,
    profileViewModel: ProfileViewModel,
    searchViewModel: SearchViewModel
){
    val inputText = remember { mutableStateOf("") }
    val user = userViewModel.GetUserByUuid(SessionManager.currentUserId.toString())

    var results by remember { mutableStateOf<List<SearchResult>>(emptyList()) }

    val personal = remember { personalViewModel.getChats(user?.uuid ?: "empty") }
    val group = user?.let { remember { groupViewModel.getGroups(it) }}
    val channel = remember (user) {
       user?.let {channelViewModel.getChannels(user = user)}
    }

    Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.black))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.statusBarsPadding())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.navigate("MainScreen") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(
                            color = colorResource(R.color.dark_blue),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(20.dp)
                    )
                    BasicTextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 48.dp, end = 48.dp)
                            .align(Alignment.CenterStart),
                        textStyle = TextStyle(
                            color = colorResource(R.color.white),
                            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                            fontSize = 16.sp
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (inputText.value.isEmpty()) {
                                Text(
                                    text = "Search",
                                    color = colorResource(R.color.white).copy(alpha = 0.7f),
                                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (inputText.value.isNotEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = "Search",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(20.dp)
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    val text = inputText.value.trim()
                                    if (text.isNotEmpty()) {
                                        val users = searchViewModel.searchUsers(query = text, currentUserId =  SessionManager.currentUserId.toString()).map { SearchResult.UserResult(it) }
                                        val groups = searchViewModel.searchGroups(text).map { SearchResult.GroupResult(it) }
                                        val channels = searchViewModel.searchChannels(text).map { SearchResult.ChannelResult(it) }
                                        results = users + groups + channels
                                    }
                                }
                        )
                    }
                }
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(results) { item ->
                    when (item) {
                        is SearchResult.UserResult -> {
                            val chatForUser = personal.find { it.user.uuid == item.user.uuid }
                            UserFindView(
                                user = item.user,
                                chat = chatForUser,
                                navController = navController,
                                profileViewModel = profileViewModel
                            )
                        }

                        is SearchResult.GroupResult -> {
                            val chatForGroup = group?.find { it.id == item.group.id}
                            GroupFindView(
                                group = item.group,
                                chat = chatForGroup,
                                navController = navController,
                                profileViewModel = profileViewModel)
                        }

                        is SearchResult.ChannelResult -> {
                            val chatForChannel = channel?.find { it.id == item.channel.id }
                            ChannelFindView(
                                channel = item.channel,
                                chat = chatForChannel,
                                navController = navController,
                                profileViewModel = profileViewModel)
                        }
                    }
                }
            }
        }
    }
}