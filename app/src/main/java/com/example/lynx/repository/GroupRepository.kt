package com.example.lynx.repository

import androidx.compose.runtime.toMutableStateList
import com.example.lynx.model.Group

object GroupRepository {
    private val _groups = mutableListOf<Group>()
    val groups = _groups

    fun createGroup(
        name: String,
        ownerId: String,
        memberIds: List<String>,
        id: String,
        avatarURI: String?
    ): Group {
        if(id.startsWith("@")) {
            val group =
                Group(
                    name = name,
                    ownerId = ownerId,
                    memberIds = memberIds.toMutableStateList(),
                    id = id,
                    avatarUri = avatarURI
                )
            _groups.add(group)
            return group
        }
        else{
            val group =
                Group(
                    name = name,
                    ownerId = ownerId,
                    memberIds = memberIds.toMutableStateList(),
                    id = "@$id", avatarUri = avatarURI
                )
            _groups.add(group)
            return group
        }
    }

    fun getGroupById(id: String): Group? = _groups.find { it.id == id }
    fun getGroupByName(name: String): Group? = _groups.find { it.name == name}
    fun getGroupByUUID(uuid: String): Group? = _groups.find { it.uuid == uuid }
    fun getGroupsById(id: String): List<Group>{
        return  _groups.filter { it.id.contains(id, ignoreCase = true) }
    }
    fun getGroupsByName(name: String): List<Group>{
        return  _groups.filter { it.name.contains(name, ignoreCase = true) }
    }
    fun getGroupsForUserUuid(uuid: String): List<Group> {
        return _groups.filter { it.memberIds.contains(uuid) || it.ownerId == uuid }
    }
    fun getUsersByGroup(group: Group?): List<String>? {
        return group?.memberIds
    }
    fun addUser(group: Group, userUuid: String) {
        group.let {
            if (!it.memberIds.contains(userUuid)) {
                it.memberIds.add(userUuid)
            }
        }
    }
    fun removeUserByGroup(userId: String, group: Group){
        if(userId == group.ownerId){
            group.ownerId = group.memberIds.first()
        }
        group.memberIds.remove(userId)
    }
    fun clear() {
        groups.clear()
    }
}