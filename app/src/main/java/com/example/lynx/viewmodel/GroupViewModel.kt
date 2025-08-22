package com.example.lynx.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.lynx.model.Channel
import com.example.lynx.model.Group
import com.example.lynx.model.User
import com.example.lynx.repository.GroupRepository
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.session.SessionManager

class GroupViewModel: ViewModel() {
    var selectedAvatarUri: Uri? = null

    var groupName = mutableStateOf("")
    var groupId = mutableStateOf("")

    val selectedMembers = mutableStateListOf<String>()

    fun getGroups(user: User): List<Group> {
        val groups = GroupRepository.getGroupsForUserUuid(user.uuid)

        return groups.map { group ->
            val messages = MessagesRepository.getMessagesForGroup(group.uuid)
            group.copy(messages = messages.toMutableList())
        }
    }
    fun getGroupById(groupId: String): Group? {
        return GroupRepository.getGroupById(groupId)
    }
    fun getGroupByUUID(uuid: String): Group?{
        return GroupRepository.getGroupByUUID(uuid)
    }

    fun addMemberInNewGroup(userId: String) {
        if (userId !in selectedMembers) {
            selectedMembers.add(userId)
        }
    }

    fun createNewGroup(
        currentUserId: String,
        groupName: String,
        groupId: String
    ){
        val avatarURI: String? = selectedAvatarUri?.toString()
        val memberIds = (selectedMembers + listOfNotNull(SessionManager.currentUserId)).distinct()

        GroupRepository.createGroup(
            name = groupName,
            ownerId = currentUserId,
            id = groupId,
            memberIds = memberIds,
            avatarURI = avatarURI
        )
        reset()
    }
    fun ApplyGroupEdit(name: String, id: String, groupUUID: String) {
        val group = getGroupByUUID(groupUUID)
        group?.name = name
        group?.id = id
        if (selectedAvatarUri != null) {
            group?.avatarUri = selectedAvatarUri.toString()
            selectedAvatarUri = null
        }
    }
    fun addMemberByGroup(userId: String, group: Group){
        GroupRepository.addUser(group,userId)
    }
    fun removeMemberByGroup(userId: String, currentUserId: String, group: Group) {
        if(userId != currentUserId) {
            GroupRepository.removeUserByGroup(userId, group)
        }
    }
    fun exitGroup(userId: String, group: Group) {
            GroupRepository.removeUserByGroup(userId, group)
    }
    fun getUsersByGroup(group: Group?): List<String>? {
        return GroupRepository.getUsersByGroup(group)
    }
    fun reset() {
        selectedAvatarUri = null
        groupName.value = ""
        groupId.value = ""
        selectedMembers.clear()
    }
    fun isOwner(group: Group?, userId: String): Boolean {
        return group?.ownerId == userId
    }
}