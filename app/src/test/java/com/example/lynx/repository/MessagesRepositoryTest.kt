package com.example.lynx.repository

import com.example.lynx.model.Message
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Assert
import org.junit.Test

class MessagesRepositoryTest {
    @After
    fun clearMessages() {
        MessagesRepository.messages.clear()
    }

    @Test
    fun addMessage_shouldStoreMessage() {
        val message = Message(senderId = "user1", groupUuid = "group1", text = "Hello!")
        MessagesRepository.addMessage(message)

        Assert.assertEquals(1, MessagesRepository.messages.size)
        Assert.assertEquals("Hello!", MessagesRepository.messages[0].text)
    }
    @Test
    fun givenDirectMessages_whenGetMessagesForUser_thenReturnDirectMessages() {
        val msg1 = Message(senderId = "u1", receiverId = "u2", text = "hi")
        val msg2 = Message(senderId = "u2", receiverId = "u1", text = "yo")
        val msg3 = Message(senderId = "u3", receiverId = "u4", text = "other")

        MessagesRepository.messages.addAll(listOf(msg1, msg2, msg3))

        val result = MessagesRepository.getMessages("u1")

        assertTrue(result.contains(msg1))
        assertTrue(result.contains(msg2))
        assertFalse(result.contains(msg3))
    }

    @Test
    fun givenGroupMessage_whenUserIsMember_thenReturnGroupMessage() {
        val group = GroupRepository.createGroup(
            name = "TestGroup",
            ownerId = "u1",
            id = "@g1",
            memberIds = mutableListOf("u2"),
            avatarURI = null
        )

        val msg = Message(senderId = "u1", groupUuid = group.uuid, text = "hello group")
        MessagesRepository.messages.add(msg)

        val result = MessagesRepository.getMessages("u2")

        assertTrue(result.contains(msg))
    }

    @Test
    fun givenChannelMessage_whenUserIsSubscribed_thenReturnChannelMessage() {
        val channel = ChannelRepository.createChannel(
            name = "TestChannel",
            ownerId = "u1",
            id = "@c1",
            memberIds = mutableListOf("u3"),
            avatarURI = null
        )

        val msg = Message(senderId = "u1", channelUuid = channel.uuid, text = "hello channel")
        MessagesRepository.messages.add(msg)

        val result = MessagesRepository.getMessages("u3")

        assertTrue(result.contains(msg))
    }

    @Test
    fun givenMessages_whenUserNotRelated_thenReturnEmpty() {
        val msg = Message(senderId = "u1", receiverId = "u2", text = "hi")
        MessagesRepository.messages.add(msg)

        val result = MessagesRepository.getMessages("u5")

        assertTrue(result.isEmpty())
    }
}