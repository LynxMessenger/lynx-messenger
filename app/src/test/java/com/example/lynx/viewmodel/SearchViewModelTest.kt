package com.example.lynx.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.example.lynx.model.Channel
import com.example.lynx.model.Group
import com.example.lynx.model.User
import com.example.lynx.repository.ChannelRepository
import com.example.lynx.repository.GroupRepository
import com.example.lynx.repository.UserRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var userA: User
    private lateinit var userB: User
    private lateinit var groupA: Group
    private lateinit var channelA: Channel

    @Before
    fun setUp() {
        viewModel = SearchViewModel()

        UserRepository.clearUsers()
        GroupRepository.clear()
        ChannelRepository.clearChannels()

        userA = User(
            uuid = "1",
            userName = "Daniel",
            id = "@dan",
            email = "dan@mail.com",
            password = "123",
            bio = ""
        )
        userB = User(
            uuid = "2",
            userName = "UserB",
            id = "@userb",
            email = "b@mail.com",
            password = "123",
            bio = ""
        )

        UserRepository.addNewUser(userA)
        UserRepository.addNewUser(userB)

        groupA = GroupRepository.createGroup(
            name = "BestGroup",
            id = "@bestgroup",
            ownerId = userA.uuid,
            avatarURI = null,
            memberIds = mutableStateListOf()
        )

        channelA = ChannelRepository.createChannel(
            name = "NewsChannel",
            id = "@newschannel",
            ownerId = userA.uuid,
            avatarURI = null,
            memberIds = mutableStateListOf()
        )
    }

    @Test
    fun givenUsername_whenSearchUsers_thenReturnMatchingUsers() {
        val result = viewModel.searchUsersByUsername("Daniel")
        assertEquals(1, result.size)
        assertEquals(userA.uuid, result[0].uuid)
    }

    @Test
    fun givenUserId_whenSearchUsers_thenReturnMatchingUsers() {
        val result = viewModel.searchUsersByUserId("@userb")
        assertEquals(1, result.size)
        assertEquals(userB.uuid, result[0].uuid)
    }

    @Test
    fun givenGroupName_whenSearchGroups_thenReturnMatchingGroups() {
        val result = viewModel.searchGroupsByName("BestGroup")
        assertEquals(1, result.size)
        assertEquals(groupA.uuid, result[0].uuid)
    }

    @Test
    fun givenGroupId_whenSearchGroups_thenReturnMatchingGroups() {
        val result = viewModel.searchGroupsById("@bestgroup")
        assertEquals(1, result.size)
        assertEquals(groupA.name, result[0].name)
    }

    @Test
    fun givenChannelName_whenSearchChannels_thenReturnMatchingChannels() {
        val result = viewModel.searchChannelsByName("NewsChannel")
        assertEquals(1, result.size)
        assertEquals(channelA.uuid, result[0].uuid)
    }

    @Test
    fun givenChannelId_whenSearchChannels_thenReturnMatchingChannels() {
        val result = viewModel.searchChannelsById("@newschannel")
        assertEquals(1, result.size)
        assertEquals(channelA.name, result[0].name)
    }

    @Test
    fun givenQuery_whenSearchUsers_thenExcludeCurrentUser() {
        val result = viewModel.searchUsers("Daniel", currentUserId = "1")
        assertTrue(result.none { it.uuid == "1" })
    }

    @Test
    fun givenQuery_whenSearchGroups_thenReturnGroups() {
        val result = viewModel.searchGroups("BestGroup")
        assertEquals(1, result.size)
    }

    @Test
    fun givenQuery_whenSearchChannels_thenReturnChannels() {
        val result = viewModel.searchChannels("NewsChannel")
        assertEquals(1, result.size)
    }
}
