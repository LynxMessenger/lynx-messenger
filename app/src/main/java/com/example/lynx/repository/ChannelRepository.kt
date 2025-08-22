package com.example.lynx.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.lynx.model.Channel


object ChannelRepository {
    private val channels = mutableListOf<Channel>()

    fun createChannel(
        name: String,
        id: String,
        ownerId: String,
        memberIds: List<String>,
        avatarURI: String?
    ): Channel {
        if(id.startsWith("@")){
            val updatedMembers = mutableStateListOf<String>().apply {
                addAll(memberIds)
                add(ownerId)
            }
        val channel = Channel(
            name = name, id = id,
            ownerId = ownerId, memberIds = updatedMembers, avatarUri = avatarURI)
        channels.add(channel)
        return channel
        }
        else{
            val updatedMembers = mutableStateListOf<String>().apply {
                addAll(memberIds)
                add(ownerId)
            }
            val channel = Channel(
                name = name, id = "@$id",
                ownerId = ownerId, memberIds = updatedMembers, avatarUri = avatarURI)
            channels.add(channel)
            return channel
        }
    }
    fun clearChannels() {
        channels.clear()
    }
    fun addMember(channelUuid: String, userUuid: String) {
        val channel = getChannelByUUID(channelUuid)
        channel?.let {
            if (!it.memberIds.contains(userUuid)) {
                it.memberIds.add(userUuid)
            }
        }
    }
    fun removeUserByChannel(userId: String, channel: Channel){
        if(userId == channel.ownerId){
            channel.ownerId = channel.memberIds.first()
        }
        channel.memberIds.remove(userId)
    }

    fun getAllChannels(): List<Channel> = channels

    fun getChannelByUUID(uuid: String):Channel? = channels.find { it.uuid == uuid }

    fun getChannelById(id: String): Channel? = channels.find { it.id == id }

    fun getChannelsForUser(uuid: String): List<Channel> = channels.filter { uuid in it.memberIds }

    fun getChannelsByName(name: String): List<Channel>{
        return channels.filter { it.name.contains(name, ignoreCase = true)}
    }
    fun getChannelsById(id: String): List<Channel>{
        return  channels.filter { it.id.contains(id, ignoreCase = true) }
    }
    fun getUsersByChannel(channel: Channel?): List<String>? {
        return channel?.memberIds
    }
    fun clear() {
        channels.clear()
    }
}