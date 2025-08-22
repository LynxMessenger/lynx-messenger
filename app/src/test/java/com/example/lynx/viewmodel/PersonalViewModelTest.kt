package com.example.lynx.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.example.lynx.model.Message
import com.example.lynx.model.User
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.repository.UserRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PersonalViewModelTest {

    private lateinit var viewModel: PersonalViewModel
    private lateinit var currentUser: User
    private lateinit var otherUser: User

    @Before
    fun setUp() {
        viewModel = PersonalViewModel()

        UserRepository.clearUsers()
        MessagesRepository.clear()

        currentUser = User(
            uuid = "u1",
            id = "@user1",
            userName = "User1",
            email = "u1@mail.com",
            password = "123",
            bio = ""
        )
        otherUser = User(
            uuid = "u2",
            id = "@user2",
            userName = "User2",
            email = "u2@mail.com",
            password = "123",
            bio = ""
        )

        UserRepository.addNewUser(currentUser)
        UserRepository.addNewUser(otherUser)
    }

    @Test
    fun givenMessagesBetweenUsers_whenGetChats_thenReturnChatList() {
        val message1 = Message(senderId = currentUser.uuid, receiverId = otherUser.uuid, text = "Hello")
        val message2 = Message(senderId = otherUser.uuid, receiverId = currentUser.uuid, text = "Hi")
        MessagesRepository.addMessage(message1)
        MessagesRepository.addMessage(message2)

        val chats = viewModel.getChats(currentUser.uuid)

        assertEquals(1, chats.size)
        val chat = chats[0]
        assertEquals(otherUser.uuid, chat.id)
        assertEquals(2, chat.messages.size)
        assertEquals(message2, chat.lastMessage)
    }

    @Test
    fun givenNoMessages_whenGetChats_thenReturnEmptyList() {
        val chats = viewModel.getChats(currentUser.uuid)
        assertTrue(chats.isEmpty())
    }

    @Test
    fun givenBlockedUser_whenGetSortedChat_thenReturnWithoutBlockedUser() {
        val blockedUser = otherUser.copy(uuid = "3", userName = "BlockedUser")
        UserRepository.addNewUser(blockedUser)

        MessagesRepository.addMessage(Message(senderId = currentUser.uuid, receiverId = otherUser.uuid, text = "Hello"))

        MessagesRepository.addMessage(Message(senderId = blockedUser.uuid, receiverId = currentUser.uuid, text = "Blocked msg"))

        val updatedCurrentUser = currentUser.copy(blockedUsers = mutableStateListOf(blockedUser.uuid))
        UserRepository.addNewUser(updatedCurrentUser)

        val chats = viewModel.getSortedChat(updatedCurrentUser.uuid)

        assertEquals(1, chats.size)
        assertEquals(otherUser.uuid, chats[0].id)
    }

    @Test
    fun givenMultipleChats_whenGetSortedChat_thenReturnChatsSortedByLastMessage() {
        val userB = User(
            uuid = "3",
            userName = "UserB",
            id = "@user3",
            email = "u3@mail.com",
            password = "123",
            bio = ""
        )
        UserRepository.addNewUser(userB)

        val msg1 = Message(senderId = otherUser.uuid, receiverId = currentUser.uuid, text = "Old", timestamp = "2025-08-18_01-30-00-000")
        val msg2 = Message(senderId = userB.uuid, receiverId = currentUser.uuid, text = "New", timestamp = "2025-08-18_01-35-00-000")
        MessagesRepository.addMessage(msg1)
        MessagesRepository.addMessage(msg2)

        val chats = viewModel.getSortedChat(currentUser.uuid)

        assertEquals(2, chats.size)
        assertEquals(userB.uuid, chats[0].id)
    }
}