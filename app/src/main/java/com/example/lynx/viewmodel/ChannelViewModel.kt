package com.example.lynx.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lynx.model.Channel
import com.example.lynx.model.User
import com.example.lynx.repository.ChannelRepository
import com.example.lynx.repository.MessagesRepository

class ChannelViewModel: ViewModel(){
    var selectedAvatarUri: Uri? = null

    var channelName = mutableStateOf("")
    var channelId = mutableStateOf("")

    val selectedMembers = mutableStateListOf<String>()

    fun reset() {
        selectedAvatarUri = null
        channelName.value = ""
        channelId.value = ""
        selectedMembers.clear()
    }

    fun getChannels(user: User): List<Channel> {
        val channels = ChannelRepository.getChannelsForUser(user.uuid)

        return channels.map { channel ->
            val messages = MessagesRepository.getMessagesForChannel(channel.uuid)
            channel.copy(messages = messages.toMutableList())
        }
    }

    fun getSortedChannel(user: User): List<Channel> {
        val channels = getChannels(user = user)

        return channels
    }

    fun getChannelById(id: String): Channel? {
        return ChannelRepository.getChannelById(id)
    }

    fun getChannelByUUID(uuid:String): Channel?{
        return ChannelRepository.getChannelByUUID(uuid)
    }

    fun createNewChannel(
        currentUserId: String,
        channelName: String,
        channelId: String
    ){

        val avatarURI: String? = selectedAvatarUri?.toString()
        val memberIds = (selectedMembers).distinct()

        ChannelRepository.createChannel(
            name = channelName,
            id = channelId,
            ownerId = currentUserId,
            memberIds = memberIds,
            avatarURI = avatarURI
        )
        reset()
    }

    fun addMemberInNewChannel(userId: String) {
        if (userId !in selectedMembers) {
            selectedMembers.add(userId)
        }
    }

    fun getUsersByChannel(channel: Channel?): List<String>? {
        return ChannelRepository.getUsersByChannel(channel)
    }

    fun ApplyChannelEdit(name: String, id: String, channelUUID: String) {
        val channel = getChannelByUUID(channelUUID)
        channel?.name = name
        channel?.id = id
        if (selectedAvatarUri != null) {
            channel?.avatarUri = selectedAvatarUri.toString()
            selectedAvatarUri = null
        }
    }

    fun addMemberByChannel(userId: String, channelId: String){
        ChannelRepository.addMember(channelId,userId)
    }

    fun removeMemberByChannel(currentUserId: String,userId: String, channel: Channel) {
        if(userId != currentUserId) {
            ChannelRepository.removeUserByChannel(userId, channel)
        }
    }

    fun exitChannel(userId: String, channel: Channel) {
            ChannelRepository.removeUserByChannel(userId, channel)
    }

    fun isOwner(channel: Channel?, userId: String): Boolean {
        return channel?.ownerId == userId
    }
}