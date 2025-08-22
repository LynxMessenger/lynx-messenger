package com.example.lynx.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lynx.view.main.MainScreen
import com.example.lynx.view.authentication.LoginScreen
import com.example.lynx.view.authentication.RegisterScreen
import com.example.lynx.view.chat.ChannelChatScreen
import com.example.lynx.view.chat.GroupChatScreen
import com.example.lynx.view.chat.PersonalChatScreen
import com.example.lynx.view.newmessages.CreateNewChannelScreen
import com.example.lynx.view.newmessages.CreateNewGroupScreen
import com.example.lynx.view.newmessages.NewMessagesScreen
import com.example.lynx.view.profile.ChannelProfileScreen
import com.example.lynx.view.profile.GroupProfileScreen
import com.example.lynx.view.profile.PersonalProfileScreen
import com.example.lynx.view.media.AvatarPreviewScreen
import com.example.lynx.view.media.ImageViewerScreen
import com.example.lynx.view.media.VideoPlayerScreen
import com.example.lynx.view.search.SearchScreen
import com.example.lynx.view.settings.addmebers.AddMemberScreen
import com.example.lynx.view.settings.personal.accountsetting.AccountSettingScreen
import com.example.lynx.view.settings.personal.editsetting.EditSettingScreen
import com.example.lynx.view.settings.personal.generalsetting.GeneralSettingScreen
import com.example.lynx.view.settings.personal.helpsetting.HelpScreen
import com.example.lynx.view.settings.personal.notificationsetting.NotificationsSettingScreen
import com.example.lynx.view.settings.personal.mainsettings.SettingScreen
import com.example.lynx.view.media.AvatarPickerFromGallery
import com.example.lynx.view.settings.channel.ChannelEditScreen
import com.example.lynx.view.settings.group.GroupEditScreen
import com.example.lynx.viewmodel.AuthorizationViewModel
import com.example.lynx.viewmodel.BlockViewModel
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.MessageViewModel
import com.example.lynx.viewmodel.PersonalViewModel
import com.example.lynx.viewmodel.ProfileViewModel
import com.example.lynx.viewmodel.SearchViewModel
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun Navigation(navController: NavHostController, isLoggedIn: Boolean) {
    val authorizationViewModel: AuthorizationViewModel = viewModel()
    val blockViewModel: BlockViewModel = viewModel()
    val channelViewModel: ChannelViewModel = viewModel()
    val groupViewModel: GroupViewModel = viewModel()
    val messageViewModel : MessageViewModel = viewModel()
    val personalViewModel: PersonalViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()

    val startDestination = if (isLoggedIn) "MainScreen" else "LoginScreen"
    NavHost(navController = navController, startDestination = startDestination) {
        composable("MainScreen") {
            MainScreen(
                navController = navController,
                personalViewModel =  personalViewModel,
                groupViewModel =  groupViewModel,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel,
                channelViewModel = channelViewModel
            )
        }
        composable("SettingScreen") {
            SettingScreen(
                navController = navController,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable("SearchScreen") {
            SearchScreen(
                navController = navController,
                userViewModel = userViewModel,
                personalViewModel = personalViewModel,
                groupViewModel = groupViewModel,
                channelViewModel = channelViewModel,
                profileViewModel = profileViewModel,
                searchViewModel =  searchViewModel
            )
        }
        composable("PersonalChatScreen/{uuid}") { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid") ?: ""
            PersonalChatScreen(
                navController =  navController,
                uuid =  uuid,
                userViewModel =  userViewModel,
                profileViewModel =  profileViewModel,
                messageViewModel = messageViewModel,
                blockViewModel = blockViewModel
            )
        }
        composable("GroupChatScreen/{groupUuid}") { backStackEntry ->
            val groupUuid = backStackEntry.arguments?.getString("groupUuid") ?: ""
            GroupChatScreen(
                groupUuid =  groupUuid,
                navController =  navController,
                groupViewModel =  groupViewModel,
                messageViewModel =  messageViewModel,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable("GroupEditScreen/{groupUuid}"){ backStackEntry ->
            val groupUuid = backStackEntry.arguments?.getString("groupUuid") ?:""
            GroupEditScreen(
                navController = navController,
                groupUuid = groupUuid,
                groupViewModel = groupViewModel,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable("ChannelChatScreen/{channelUuid}") { backStackEntry ->
            val channelUuid = backStackEntry.arguments?.getString("channelUuid") ?: ""
            ChannelChatScreen(
                navController = navController,
                channelUuid = channelUuid,
                userViewModel = userViewModel,
                channelViewModel =  channelViewModel,
                messageViewModel =  messageViewModel,
                profileViewModel =  profileViewModel
            )
        }
        composable("PersonalProfileScreen/{uuid}") { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid") ?: ""

            PersonalProfileScreen(
                navController =  navController,
                uuid =  uuid,
                blockViewModel = blockViewModel,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable("GroupProfileScreen/{groupUuid}") { backStackEntry ->
            val groupUuid = backStackEntry.arguments?.getString("groupUuid") ?: ""

            GroupProfileScreen(
                navController =  navController,
                groupUuid =  groupUuid,
                groupViewModel =  groupViewModel,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel,
            )
        }
        composable("ChannelProfileScreen/{channelUuid}") { backStackEntry ->
            val channelUuid = backStackEntry.arguments?.getString("channelUuid") ?: ""

            ChannelProfileScreen(
                navController =  navController,
                channelUuid = channelUuid,
                userViewModel = userViewModel,
                channelViewModel = channelViewModel,
                profileViewModel = profileViewModel,
            )
        }
        composable("GeneralSettingScreen") {
            GeneralSettingScreen(
               navController =  navController
            )
        }
        composable("AccountSettingScreen") {
            AccountSettingScreen(
               navController = navController,
               userViewModel =  userViewModel,
            )
        }
        composable("NotificationsSettingScreen") {
            NotificationsSettingScreen(
               navController =  navController
            )
        }
        composable("HelpScreen") {
            HelpScreen(
                navController =  navController
            )
        }
        composable("EditSettingScreen") {
            EditSettingScreen(
                navController =  navController,
                userViewModel =  userViewModel,
                profileViewModel = profileViewModel,
            )
        }
        composable("LoginScreen") {
            LoginScreen(
                navController,
                authorizationViewModel = authorizationViewModel
            )
        }
        composable("RegisterScreen") {
            RegisterScreen(
                navController,
                authorizationViewModel = authorizationViewModel
            )
        }
        composable(
            route = "avatarPreview/{uuid}?type={type}",
            arguments = listOf(
                navArgument("uuid") { type = NavType.StringType },
                navArgument("type") {
                    type = NavType.StringType
                    defaultValue = "user"
                }
            )
        ) { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid") ?: ""
            val type = backStackEntry.arguments?.getString("type") ?: "user"

            AvatarPreviewScreen(
                navController = navController,
                uuid = uuid,
                isGroup = type == "group",
                isChannel = type == "channel",
                userViewModel = userViewModel,
                groupViewModel = groupViewModel,
                channelViewModel = channelViewModel,
                profileViewModel = profileViewModel
            )
        }
        composable("avatarPicker") {
           AvatarPickerFromGallery(
               navController = navController,
               userViewModel = userViewModel,
               groupViewModel = groupViewModel,
               channelViewModel = channelViewModel,
            )
        }
        composable("avatarPickerGroup") {
            AvatarPickerFromGallery(
                navController = navController,
                userViewModel = userViewModel,
                groupViewModel = groupViewModel,
                channelViewModel = channelViewModel,
                isForGroup = true,
            )
        }
        composable("avatarPickerChannel") {
            AvatarPickerFromGallery(
                navController = navController,
                userViewModel = userViewModel,
                groupViewModel = groupViewModel,
                channelViewModel = channelViewModel,
                isForChannel = true,
            )
        }
        composable("NewMessagesScreen"){
            NewMessagesScreen(
                navController =  navController,
                personalViewModel =  personalViewModel,
                profileViewModel = profileViewModel,
            )
        }
        composable("CreateNewGroup"){
            CreateNewGroupScreen(
                navController =  navController,
                userViewModel = userViewModel,
                groupViewModel =  groupViewModel,
                personalViewModel =  personalViewModel,
                profileViewModel = profileViewModel,
            )
        }
        composable ("CreateNewChannel"){
            CreateNewChannelScreen(
                navController =  navController,
                channelViewModel = channelViewModel,
                personalViewModel =  personalViewModel,
                userViewModel = userViewModel,
                profileViewModel = profileViewModel,
            )
        }
        composable("AddMemberGroup/{groupUuid}") { backStackEntry ->
            val groupUuid = backStackEntry.arguments?.getString("groupUuid") ?: ""
            var group = groupViewModel.getGroupByUUID(groupUuid)
            AddMemberScreen(
                navController = navController,
                groupViewModel = groupViewModel,
                personalViewModel = personalViewModel,
                searchViewModel = searchViewModel,
                profileViewModel = profileViewModel,
                channelViewModel = channelViewModel,
                group = group
            )
        }
        composable("AddMemberChannel/{channelUuid}") { backStackEntry ->
            val channelUuid = backStackEntry.arguments?.getString("channelUuid")
            if(channelUuid != null) {
                var channel = channelViewModel.getChannelByUUID(channelUuid)
                AddMemberScreen(
                    navController = navController,
                    groupViewModel = groupViewModel,
                    personalViewModel = personalViewModel,
                    searchViewModel = searchViewModel,
                    profileViewModel = profileViewModel,
                    channelViewModel = channelViewModel,
                    channel = channel
                )
            }
        }
        composable("ChannelEditScreen/{channelUuid}") {backStackEntry ->
            val channelUuid = backStackEntry.arguments?.getString("channelUuid")
            if(channelUuid != null)
            ChannelEditScreen(
                channelUuid = channelUuid,
                navController = navController,
                channelViewModel = channelViewModel,
                profileViewModel = profileViewModel,
                userViewModel = userViewModel
            )
        }
        composable("videoPlayer/{videoUri}") { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("videoUri") ?: return@composable
            val uri = Uri.parse(uriString)
            VideoPlayerScreen(
                navController = navController,
                uri = uri
            )
        }
        composable("ImageViewerScreen/{imageUri}") { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("imageUri") ?: return@composable
            val uri = Uri.parse(uriString)
            ImageViewerScreen(imageUri = uri) {
                navController.popBackStack()
            }
        }
    }
}