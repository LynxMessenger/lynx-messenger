package com.example.lynx.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import com.example.lynx.model.Channel
import com.example.lynx.model.Message
import com.example.lynx.model.User
import com.example.lynx.repository.ChannelRepository
import com.example.lynx.repository.MessagesRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ChannelViewModelTest {

    private lateinit var viewModel: ChannelViewModel
    private lateinit var currentUser: User
    private lateinit var testChannel: Channel

    @Before
    fun setUp() {
        viewModel = ChannelViewModel()

        currentUser = User(
            uuid = "u1",
            id = "@user",
            userName = "User",
            email = "user@mail.com",
            password = "123",
            bio = ""
        )

        testChannel = Channel(
            uuid = "c1",
            name = "TestChannel",
            id = "@test",
            ownerId = currentUser.uuid,
            memberIds = mutableStateListOf<String>().apply {
                addAll(listOf("user1", "user2"))
            },
            avatarUri = null,
            messages = mutableListOf()
        )

        ChannelRepository.clear()
        MessagesRepository.clear()
    }

    @Test
    fun givenNonEmptyState_whenReset_thenClearsAllFields() {
        viewModel.selectedAvatarUri = Uri.parse("test://avatar")
        viewModel.channelName.value = "Name"
        viewModel.channelId.value = "Id"
        viewModel.selectedMembers.add("m1")

        viewModel.reset()

        assertNull(viewModel.selectedAvatarUri)
        assertEquals("", viewModel.channelName.value)
        assertEquals("", viewModel.channelId.value)
        assertTrue(viewModel.selectedMembers.isEmpty())
    }

    @Test
    fun givenSelectedAvatarAndMembers_whenCreateNewChannel_thenChannelIsCreatedAndStateIsReset() {
        viewModel.selectedAvatarUri = Uri.parse("test://avatar")
        viewModel.selectedMembers.addAll(listOf("m1", "m2"))

        viewModel.createNewChannel(
            currentUserId = currentUser.uuid,
            channelName = "ChannelName",
            channelId = "@chan"
        )

        val createdChannel = ChannelRepository.getChannelsForUser(currentUser.uuid).firstOrNull()
        assertNotNull(createdChannel)
        assertEquals("ChannelName", createdChannel!!.name)
        assertEquals("@chan", createdChannel.id)
        assertEquals("test://avatar", createdChannel.avatarUri)
        assertTrue(viewModel.selectedMembers.isEmpty())
    }

    @Test
    fun givenMemberAlreadyInNewChannel_whenAddMember_thenDoNotDuplicate() {
        viewModel.selectedMembers.add("m1")

        viewModel.addMemberInNewChannel("m1")
        assertEquals(1, viewModel.selectedMembers.size)

        viewModel.addMemberInNewChannel("m2")
        assertEquals(2, viewModel.selectedMembers.size)
    }

    @Test
    fun givenChannelWithMessages_whenGetChannels_thenReturnChannelsWithMessages() {
        ChannelRepository.createChannel("Test", "@t", currentUser.uuid, listOf(), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        MessagesRepository.addMessage(
            Message(
                senderId = currentUser.uuid,
                channelUuid = created.uuid,
                text = "Hi"
            )
        )

        val result = viewModel.getChannels(currentUser)

        assertEquals(1, result.size)
        assertEquals(1, result.first().messages.size)
    }

    @Test
    fun givenExistingChannel_whenGetChannelById_thenReturnCorrectChannel() {
        ChannelRepository.createChannel("Test", "@t", currentUser.uuid, listOf(), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        val found = viewModel.getChannelById(created.id)

        assertEquals(created.uuid, found?.uuid)
    }

    @Test
    fun givenExistingChannel_whenGetChannelByUUID_thenReturnCorrectChannel() {
        ChannelRepository.createChannel("Test", "@t", currentUser.uuid, listOf(), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        val found = viewModel.getChannelByUUID(created.uuid)

        assertEquals(created.id, found?.id)
    }

    @Test
    fun givenExistingChannel_whenApplyChannelEdit_thenUpdateNameIdAndAvatar() {
        ChannelRepository.createChannel("OldName", "@old", currentUser.uuid, listOf(), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        viewModel.selectedAvatarUri = Uri.parse("test://avatar")
        viewModel.ApplyChannelEdit("NewName", "@new", created.uuid)

        val updated = ChannelRepository.getChannelByUUID(created.uuid)
        assertEquals("NewName", updated?.name)
        assertEquals("@new", updated?.id)
        assertEquals("test://avatar", updated?.avatarUri)
    }

    @Test
    fun givenChannelWithoutMember_whenAddMemberByChannel_thenMemberIsAdded() {
        ChannelRepository.createChannel("Test", "@t", currentUser.uuid, listOf(), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        viewModel.addMemberByChannel("u2", created.uuid)

        val updated = ChannelRepository.getChannelByUUID(created.uuid)
        assertTrue(updated!!.memberIds.contains("u2"))
    }

    @Test
    fun givenChannelAndNonOwner_whenRemoveMemberByChannel_thenMemberIsRemoved() {
        ChannelRepository.createChannel("Test", "@t", currentUser.uuid, listOf("u1", "u2"), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        viewModel.removeMemberByChannel("u1", "u2", created)

        val updated = ChannelRepository.getChannelByUUID(created.uuid)
        assertFalse(updated!!.memberIds.contains("u2"))
    }

    @Test
    fun givenChannel_whenExitChannel_thenUserIsRemovedFromMembers() {
        ChannelRepository.createChannel("Test", "@t", currentUser.uuid, listOf("u1", "u2"), null)
        val created = ChannelRepository.getChannelsForUser(currentUser.uuid).first()

        viewModel.exitChannel("u2", created)

        val updated = ChannelRepository.getChannelByUUID(created.uuid)
        assertFalse(updated!!.memberIds.contains("u2"))
    }

    @Test
    fun givenChannel_whenCheckOwner_thenReturnTrueIfOwnerElseFalse() {
        assertTrue(viewModel.isOwner(testChannel, currentUser.uuid))
        assertFalse(viewModel.isOwner(testChannel, "u2"))
    }
}
