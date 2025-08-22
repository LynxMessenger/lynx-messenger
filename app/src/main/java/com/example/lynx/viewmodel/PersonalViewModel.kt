package com.example.lynx.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lynx.model.Chat
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.repository.UserRepository

class PersonalViewModel: ViewModel() {
    fun getChats(currentUserId: String): List<Chat> {
        val users = UserRepository.getAllUsers()
        val messages = MessagesRepository.messages

        return users.mapNotNull { user ->
            if (user.uuid == currentUserId) return@mapNotNull null

            val userMessages = messages.filter {
                (it.senderId == user.uuid && it.receiverId == currentUserId) ||
                        (it.senderId == currentUserId && it.receiverId == user.uuid)
            }

            if (userMessages.isNotEmpty()) {
                Chat(
                    id = user.uuid,
                    userId = listOf(currentUserId, user.uuid),
                    lastMessage = userMessages.lastOrNull(),
                    messages = userMessages.toMutableList(),
                    user = user
                )
            } else null
        }
    }
    fun getSortedChat(currentUserId: String): List<Chat> {
        val currentUser = UserRepository.getUserByUuid(currentUserId)
        val chats = getChats(currentUserId)

        val visibleChats = chats.filterNot { chat ->
            currentUser?.blockedUsers?.contains(chat.user.uuid) == true
        }
        return visibleChats.sortedByDescending { it.lastMessage?.timestamp }
    }
}