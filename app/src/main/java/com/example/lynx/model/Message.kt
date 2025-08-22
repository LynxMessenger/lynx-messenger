package com.example.lynx.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Message(
    val senderId: String,
    val receiverId: String? = null,
    val groupUuid: String? = null,
    val channelUuid: String? = null,
    val text: String? = null,
    val imageUri: String? = null,
    val audioUri: String? = null,
    val videoUri: String? = null,
    val timestamp: String = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.getDefault()).format(Date()),
)