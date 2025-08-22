package com.example.lynx.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.lynx.R
import java.util.UUID

data class Group(
    val uuid: String = UUID.randomUUID().toString(),
    var id: String,
    var name: String,
    var avatarUri: String? = null,
    var ownerId: String,
    val memberIds: SnapshotStateList<String> = mutableStateListOf(),
    var messages: MutableList<Message> = mutableListOf()
)