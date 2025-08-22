package com.example.lynx.ui

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.getValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.lynx.model.Message
import com.example.lynx.navigation.Navigation
import com.example.lynx.repository.ChannelRepository
import com.example.lynx.repository.GroupRepository
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.repository.UserRepository
import com.example.lynx.session.SessionManager

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
            }
        }
        SessionManager.init(applicationContext)

        if (UserRepository.users.isEmpty()) {
            UserRepository.users.addAll(UserRepository.firstUsers)
        }
        MessagesRepository.initMessages(UserRepository.users)

        val testGroup = GroupRepository.createGroup(
            name = "test",
            id = "@test",
            ownerId = UserRepository.users[4].uuid,
            memberIds = listOf(UserRepository.users[0].uuid, UserRepository.users[1].uuid,UserRepository.users[4].uuid),
            avatarURI = null
        )
        MessagesRepository.addMessage(
            Message(
                senderId = UserRepository.users[0].uuid,
                groupUuid = GroupRepository.groups[0].uuid,
                text = "Привет всем!",
            )
        )
        MessagesRepository.addMessage(
            Message(
                senderId = UserRepository.users[1].uuid,
                groupUuid = GroupRepository.groups[0].uuid,
                text = "привет"
            )
        )
        MessagesRepository.addMessage(
            Message(
                senderId = UserRepository.users[0].uuid,
                groupUuid = GroupRepository.groups[0].uuid,
                text = "как дела?"
            )
        )
        val testChannel = ChannelRepository.createChannel(
            name = "testChannel",
            id = "@testChannel",
            ownerId = UserRepository.users[4].uuid,
            memberIds =listOf(UserRepository.users[0].uuid, UserRepository.users[1].uuid),
            avatarURI = null
        )
        ChannelRepository.addMember(ChannelRepository.getChannelById("@testChannel")?.uuid ?: "", UserRepository.users[0].uuid)

        MessagesRepository.addMessage(
            Message(
                senderId = UserRepository.users[4].uuid,
                channelUuid = testChannel.uuid,
                text = "test channel"
            )
        )
        setContent {
            val navController = rememberNavController()
            val isLoggedIn by SessionManager.isLoggedIn

            Navigation(
                navController = navController,
                isLoggedIn = isLoggedIn
            )
        }
    }
}