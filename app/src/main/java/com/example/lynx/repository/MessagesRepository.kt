package com.example.lynx.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.lynx.model.Message
import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository.users
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MessagesRepository {
    @SuppressLint("ConstantLocale")
    val messages = mutableStateListOf<Message>()

    fun getMessages(userUuid: String): List<Message> {
        val userGroups = GroupRepository.getGroupsForUserUuid(userUuid).map { it.uuid }.toSet()
        val userChannels = ChannelRepository.getChannelsForUser(userUuid).map { it.uuid }.toSet()

        return messages.filter { message ->
            message.senderId == userUuid ||
                    message.receiverId == userUuid ||
                    (message.groupUuid in userGroups) ||
                    (message.channelUuid in userChannels)
        }
    }
    fun getMessagesForGroup(groupUuid: String): List<Message> {
        return messages.filter { it.groupUuid == groupUuid }
    }
    fun getMessagesForChannel(channelUuid: String): List<Message> {
        return messages.filter { it.channelUuid == channelUuid }
    }
    fun addMessage(message: Message) {
        messages.add(message)
    }

    fun initMessages(users: List<User>) {
        if (users.size > 3) {
            messages.clear()
            messages.addAll(
                listOf(
                    Message(
                        senderId = users[1].uuid,
                        receiverId = users[4].uuid,
                        text = "hello!",
                        timestamp = "2025-08-01_00-04-00-001"
                    ),
                    Message(
                        senderId = users[1].uuid,
                        receiverId = users[4].uuid,
                        text = "how are u?",
                        timestamp = "2025-08-01_03-04-22-583"
                    ),
                    Message(
                        senderId = users[0].uuid,
                        receiverId = users[4].uuid,
                        text = "Hi bro!",
                        timestamp = "2025-07-31_22-30-11-742"
                    ),
                    Message(
                        senderId = users[2].uuid,
                        receiverId = users[4].uuid,
                        text = "привет!",
                        timestamp = "2025-08-01_02-04-47-309"
                    ),
                    Message(
                        senderId = users[3].uuid,
                        receiverId = users[4].uuid,
                        text = "Как дела?",
                        timestamp = "2025-08-01_03-04-59-921"
                    ),
                )
            )
        }
    }
    fun clear() {
        messages.clear()
    }
}