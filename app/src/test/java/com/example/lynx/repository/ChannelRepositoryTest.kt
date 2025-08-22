package com.example.lynx.repository

import com.example.lynx.model.Channel
import org.junit.After
import org.junit.Assert
import org.junit.Test

class ChannelRepositoryTest {
    @After
    fun clearChannels() {
        ChannelRepository.clearChannels()
    }
    @Test
    fun createChannel_shouldReturnCorrectChannelWithAt(){
        val channel = ChannelRepository.createChannel("Test", "@123", "owner1", listOf(), null)
        Assert.assertEquals("@123", channel.id)
        Assert.assertTrue(channel.memberIds.contains("owner1"))
        Assert.assertEquals(1, channel.memberIds.size)
        Assert.assertNull(channel.avatarUri)
        Assert.assertEquals("Test", channel.name)
        Assert.assertEquals("owner1", channel.ownerId)
    }
    @Test
    fun createChannel_shouldReturnCorrectChannelWithoutAt(){
        val channel = ChannelRepository.createChannel("Test", "123", "owner1", listOf(), null)
        Assert.assertEquals("@123", channel.id)
        Assert.assertTrue(channel.memberIds.contains("owner1"))
        Assert.assertEquals(1, channel.memberIds.size)
        Assert.assertNull(channel.avatarUri)
        Assert.assertEquals("Test", channel.name)
        Assert.assertEquals("owner1", channel.ownerId)
    }
    @Test
    fun addMember_shouldAddOnlyOneUser(){
        val channel = ChannelRepository.createChannel("test", "123","owner1", listOf(), null)
        val uuid = channel.uuid
        ChannelRepository.addMember(uuid, "user1")
        val sizeAfterFirstAdd = channel.memberIds.size
        ChannelRepository.addMember(uuid, "user1")
        val sizeAfterSecondAdd = channel.memberIds.size

        Assert.assertTrue(channel.memberIds.contains("user1"))
        Assert.assertEquals(sizeAfterFirstAdd, sizeAfterSecondAdd)
    }
    @Test
    fun removeUser_shouldRemoveCorrectUser(){
        val channel = ChannelRepository.createChannel("test", "123","owner1", listOf(), null)
        val uuid = channel.uuid
        ChannelRepository.addMember(uuid, "user1")
        Assert.assertTrue(channel.memberIds.contains("user1"))
        ChannelRepository.removeUserByChannel(userId = "user1", channel)
        Assert.assertFalse(channel.memberIds.contains("user1"))
        Assert.assertTrue(channel.memberIds.contains("owner1"))
        Assert.assertEquals(1, channel.memberIds.size)
    }
    @Test
    fun getAllChannels_shouldGetCorrectChannels(){
        val channel1 = ChannelRepository.createChannel("test1", "1","owner1", listOf(), null)
        val channel2 = ChannelRepository.createChannel("test2", "2","owner3", listOf(), null)
        val channel3 = ChannelRepository.createChannel("test3", "3","owner4", listOf(), null)
        val channels = ChannelRepository.getAllChannels()
        Assert.assertEquals(3, channels.size)
        Assert.assertTrue(channels.contains(channel1))
        Assert.assertTrue(channels.contains(channel2))
        Assert.assertTrue(channels.contains(channel3))
    }
    @Test
    fun getChannelByUUID_shouldGetCorrectChannel(){
        val channel = ChannelRepository.createChannel("test", "123", "owner1", listOf(), null)
        val uuid = channel.uuid
        val findChannel = ChannelRepository.getChannelByUUID(uuid)
        Assert.assertSame(channel, findChannel)
    }
    @Test
    fun getChannelByUUID_shouldGetNull(){
        Assert.assertNull(ChannelRepository.getChannelByUUID("non-existent-uuid"))
    }
    @Test
    fun getChannelByID_shouldGetCorrectChannel(){
        val channel = ChannelRepository.createChannel("test", "123", "owner1", listOf(), null)
        val id = channel.id
        val findChannel = ChannelRepository.getChannelById(id)
        Assert.assertSame(channel, findChannel)
    }
    @Test
    fun getChannelByID_shouldGetNull(){
        Assert.assertNull(ChannelRepository.getChannelById("non-existent-id"))
    }
    @Test
    fun getChannelsForUser_shouldGetCorrectChannels() {
        val channel1 = ChannelRepository.createChannel("test1", "1","owner1", listOf(), null)
        val channel2 = ChannelRepository.createChannel("test2", "2","owner3", listOf(), null)
        val channel3 = ChannelRepository.createChannel("test3", "3","owner4", listOf(), null)

        ChannelRepository.addMember(channel1.uuid, "user1")
        ChannelRepository.addMember(channel2.uuid, "user1")
        ChannelRepository.addMember(channel3.uuid, "user1")

        val channels = ChannelRepository.getChannelsForUser("user1")

        Assert.assertEquals(3, channels.size)
        Assert.assertTrue(channels.containsAll(listOf(channel1, channel2, channel3)))
    }
    @Test
    fun getChannelsForUser_shouldGetNull(){
        val channels = ChannelRepository.getChannelsForUser("non-exists-user")
        Assert.assertTrue(channels.isEmpty())
    }
    @Test
    fun getChannelsByName_shouldFindIgnoringCase() {
        val channel1 = ChannelRepository.createChannel("TestOne", "1", "owner1", listOf(), null)
        val channel2 = ChannelRepository.createChannel("AnotherTest", "2", "owner2", listOf(), null)

        val found = ChannelRepository.getChannelsByName("testone")
        Assert.assertTrue(found.contains(channel1))
        Assert.assertFalse(found.contains(channel2))
    }

    @Test
    fun getChannelsByName_shouldReturnEmptyListIfNoMatches() {
        ChannelRepository.createChannel("Alpha", "1", "owner1", listOf(), null)
        val found = ChannelRepository.getChannelsByName("doesnotexist")
        Assert.assertTrue(found.isEmpty())
    }

    @Test
    fun getChannelsById_shouldFindBySubstring() {
        val channel1 = ChannelRepository.createChannel("TestOne", "abc123", "owner1", listOf(), null)
        val channel2 = ChannelRepository.createChannel("Another", "xyz789", "owner2", listOf(), null)

        val found = ChannelRepository.getChannelsById("abc")
        Assert.assertTrue(found.contains(channel1))
        Assert.assertFalse(found.contains(channel2))
    }

    @Test
    fun getChannelsById_shouldReturnEmptyListIfNoMatches() {
        ChannelRepository.createChannel("Test", "123", "owner1", listOf(), null)
        val found = ChannelRepository.getChannelsById("zzz")
        Assert.assertTrue(found.isEmpty())
    }

    @Test
    fun getUsersByChannel_shouldReturnCorrectMembers() {
        val channel = ChannelRepository.createChannel("Test", "1", "owner1", listOf("user1", "user2"), null)
        val members = ChannelRepository.getUsersByChannel(channel)
        Assert.assertEquals(listOf("user1", "user2", "owner1"), members)
    }

    @Test
    fun getUsersByChannel_shouldReturnNullIfChannelIsNull() {
        val members = ChannelRepository.getUsersByChannel(null)
        Assert.assertNull(members)
    }
}