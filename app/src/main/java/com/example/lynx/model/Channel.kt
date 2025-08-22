package com.example.lynx.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID

data class Channel(
    var id: String,
    val uuid: String = UUID.randomUUID().toString(),
    var name: String,
    var ownerId: String,
    val memberIds: SnapshotStateList<String> = mutableStateListOf(),
    val messages: MutableList<Message> = mutableListOf(),
    var avatarUri: String? = null,
)