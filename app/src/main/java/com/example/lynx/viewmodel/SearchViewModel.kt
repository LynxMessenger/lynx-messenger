package com.example.lynx.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lynx.model.Channel
import com.example.lynx.model.Group
import com.example.lynx.model.User
import com.example.lynx.repository.ChannelRepository
import com.example.lynx.repository.GroupRepository
import com.example.lynx.repository.UserRepository

class SearchViewModel : ViewModel() {
    fun searchUsersByUsername(username: String): List<User> {
        return UserRepository.getUsersByName(username)
    }

    fun searchUsersByUserId(id: String): List<User> {
        return UserRepository.getUsersById(id)
    }
    fun searchGroupsByName(groupname: String): List<Group> {
        return GroupRepository.getGroupsByName(groupname)
    }
    fun searchGroupsById(groupId: String): List<Group> {
        return GroupRepository.getGroupsById(groupId)
    }
    fun searchChannelsByName(channelname: String): List<Channel> {
        return ChannelRepository.getChannelsByName(channelname)
    }
    fun searchChannelsById(channelId: String): List<Channel> {
        return ChannelRepository.getChannelsById(channelId)
    }
    fun searchUsers(query: String, currentUserId: String): List<User> {
        return (searchUsersByUsername(query) + searchUsersByUserId(query))
            .filter { it.uuid != currentUserId }
            .distinct()
    }
    fun searchGroups(query: String): List<Group> {
        return (searchGroupsByName(query) + searchGroupsById(query)).distinct()
    }
    fun searchChannels(query: String): List<Channel> {
        return (searchChannelsByName(query) + searchChannelsById(query)).distinct()
    }
}