package com.example.lynx.viewmodel

import com.example.lynx.model.Message
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.test.core.app.ApplicationProvider
import com.example.lynx.model.Channel
import com.example.lynx.model.User
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.repository.UserRepository
import com.example.lynx.session.SessionManager
import com.example.lynx.utils.FakeMediaStorage
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class MessageViewModelTest {

    private lateinit var viewModel: MessageViewModel
    private lateinit var context: Context
    private lateinit var currentUser: User
    private lateinit var otherUser: User

    @Before
    fun setUp() {
        viewModel = MessageViewModel(mediaStorage = FakeMediaStorage())
        context = ApplicationProvider.getApplicationContext()

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
        SessionManager.setCurrentUserId(currentUser.uuid)
    }

    // region sendMessage

    @Test
    fun givenDirectMessageAndNoBlocks_whenSendMessage_thenMessageIsStored() {
        viewModel.sendMessage(senderId = currentUser.uuid, receiverId = otherUser.uuid, text = "hi")
        val messages = MessagesRepository.getMessages(currentUser.uuid)
        assertEquals(1, messages.size)
        assertEquals("hi", messages.first().text)
    }

    @Test
    fun givenBlockedBySender_whenSendMessage_thenMessageNotStored() {
        currentUser.blockedUsers.add(otherUser.uuid)
        viewModel.sendMessage(senderId = currentUser.uuid, receiverId = otherUser.uuid, text = "hi")
        assertTrue(MessagesRepository.getMessages(currentUser.uuid).isEmpty())
    }

    @Test
    fun givenBlockedByReceiver_whenSendMessage_thenMessageNotStored() {
        otherUser.blockedUsers.add(currentUser.uuid)
        viewModel.sendMessage(senderId = currentUser.uuid, receiverId = otherUser.uuid, text = "hi")
        assertTrue(MessagesRepository.getMessages(currentUser.uuid).isEmpty())
    }

    @Test
    fun givenGroupUuid_whenSendMessage_thenMessageStoredInGroup() {
        viewModel.sendMessage(senderId = currentUser.uuid, groupUuid = "g1", text = "group hi")
        val msg = MessagesRepository.getMessages(currentUser.uuid).first()
        assertEquals("g1", msg.groupUuid)
    }

    @Test
    fun givenChannelUuid_whenSendMessage_thenMessageStoredInChannel() {
        viewModel.sendMessage(senderId = currentUser.uuid, channelUuid = "c1", text = "channel hi")
        val msg = MessagesRepository.getMessages(currentUser.uuid).first()
        assertEquals("c1", msg.channelUuid)
    }

    // endregion

    // region sendMessageWithMedia

    @Test
    fun givenImageUri_whenSendMessageWithMediaToUser_thenImageMessageStored() {
        val uri = Uri.parse("file://dummy/test.jpg")
        viewModel.sendMessageWithMedia(context, uri,isGroup = false, isChannel = false, uuid = otherUser.uuid, channel = null)
        val msg = MessagesRepository.getMessages(currentUser.uuid).first()
        assertTrue(msg.imageUri?.contains("chat_images") == true)
    }

    @Test
    fun givenVideoUri_whenSendMessageWithMediaToGroup_thenVideoMessageStored() {
        val uri = Uri.parse("file://dummy/test.mp4")
        viewModel.sendMessageWithMedia(context, uri, isGroup = true, isChannel = false, uuid = "g1", channel = null)
        val msg = MessagesRepository.getMessages(currentUser.uuid).first()
        assertTrue(msg.videoUri?.contains("chat_videos") == true)
    }

    @Test
    fun givenAudioUri_whenSendMessageWithMediaToChannel_thenAudioMessageStored() {
        val uri = Uri.parse("file://dummy/test.m4a")
        val channel = Channel(uuid = "c1", name = "Test",id = "@t", ownerId = currentUser.uuid)
        viewModel.sendMessageWithMedia(context, uri, isGroup = false, isChannel = true, uuid = "c1", channel = channel)
        val msg = MessagesRepository.getMessages(currentUser.uuid).first()
        assertTrue(msg.audioUri?.contains("chat_audios") == true)
    }

    @Test
    fun givenUnknownFile_whenSendMessageWithMedia_thenNothingStored() {
        val uri = Uri.parse("file://dummy/test.xyz")
        viewModel.sendMessageWithMedia(context, uri, isGroup = false, isChannel = false, uuid = otherUser.uuid, channel = null)
        assertTrue(MessagesRepository.getMessages(currentUser.uuid).isEmpty())
    }

    // endregion

    // region getMessages

    @Test
    fun givenUserConversation_whenGetMessages_thenReturnMessagesBetweenUsers() {
        MessagesRepository.addMessage(Message(senderId = currentUser.uuid, receiverId = otherUser.uuid, text = "hi"))
        val result = viewModel.getMessages(currentUserId = currentUser.uuid, userId = otherUser.uuid)
        assertEquals(1, result.size)
    }

    @Test
    fun givenGroupMessages_whenGetMessages_thenReturnGroupMessages() {
        MessagesRepository.addMessage(Message(senderId = currentUser.uuid, groupUuid = "g1", text = "hello"))
        val result = viewModel.getMessages(currentUserId = currentUser.uuid, groupUuid = "g1")
        assertEquals(1, result.size)
    }

    @Test
    fun givenChannelMessages_whenGetMessages_thenReturnChannelMessages() {
        MessagesRepository.addMessage(Message(senderId = currentUser.uuid, channelUuid = "c1", text = "hello"))
        val result = viewModel.getMessages(currentUserId = currentUser.uuid, channelUuid = "c1")
        assertEquals(1, result.size)
    }

    @Test
    fun givenNoFilter_whenGetMessages_thenReturnEmptyList() {
        val result = viewModel.getMessages(currentUserId = currentUser.uuid)
        assertTrue(result.isEmpty())
    }
}
