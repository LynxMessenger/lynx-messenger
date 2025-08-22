package com.example.lynx.viewmodel

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.lynx.model.Channel
import com.example.lynx.model.Message
import com.example.lynx.repository.MessagesRepository
import com.example.lynx.repository.MessagesRepository.messages
import com.example.lynx.repository.UserRepository
import com.example.lynx.session.SessionManager
import com.example.lynx.utils.MediaStorage
import com.example.lynx.utils.RealMediaStorage
import com.example.lynx.utils.saveFileToInternalStorage

class MessageViewModel(
    private val mediaStorage: MediaStorage = RealMediaStorage()
) : ViewModel() {
    @OptIn(UnstableApi::class)
    fun sendMessage(
        senderId: String,
        receiverId: String? = null,
        groupUuid: String? = null,
        channelUuid: String? = null,
        text: String? = null,
        videoUri: Uri? = null,
        imageUri: Uri? = null,
        audioUri: Uri? = null
    ) {
        Log.d("MessageViewModel", "sendMessage called with audioUri = $audioUri, text = $text")
        val sender = UserRepository.getUserByUuid(senderId)

        if (groupUuid != null) {
            val message = Message(
                senderId = senderId,
                groupUuid = groupUuid,
                text = text,
                videoUri = videoUri?.toString(),
                audioUri = audioUri?.toString(),
                imageUri = imageUri?.toString(),
            )
            MessagesRepository.addMessage(message)
            Log.d("MessageViewModel", "Message added: $message")
            return
        }

        if (channelUuid != null) {
            val message = Message(
                senderId = senderId,
                channelUuid = channelUuid,
                text = text,
                videoUri = videoUri?.toString(),
                audioUri = audioUri?.toString(),
                imageUri = imageUri?.toString(),
            )
            MessagesRepository.addMessage(message)
            return
        }

        if (receiverId != null) {
            val receiver = UserRepository.getUserByUuid(receiverId)
            if (sender?.blockedUsers?.contains(receiverId) != true &&
                receiver?.blockedUsers?.contains(senderId) != true
            ) {
                val message = Message(
                    senderId = senderId,
                    receiverId = receiverId,
                    text = text,
                    videoUri = videoUri?.toString(),
                    audioUri = audioUri?.toString(),
                    imageUri = imageUri?.toString(),
                )
                MessagesRepository.addMessage(message)
            }
        }
    }
    fun sendMessageWithMedia(
        context: Context,
        uri: Uri,
        isGroup: Boolean,
        isChannel: Boolean,
        uuid: String,
        channel: Channel?
    ) {
        val type = context.contentResolver.getType(uri)
            ?: guessTypeFromExtension(uri.path ?: "")

        val savedUri = when {
            type?.startsWith("video") == true -> mediaStorage.saveVideo(context, uri)
            type?.startsWith("image") == true -> mediaStorage.saveImage(context, uri)
            type?.startsWith("audio") == true -> mediaStorage.saveAudio(context, uri)
            else -> null
        }

        savedUri?.let { saved ->
            val currentUserId = SessionManager.currentUserId.toString()
            when {
                isGroup -> sendMessage(
                    senderId = currentUserId,
                    groupUuid = uuid,
                    videoUri = if (type?.startsWith("video") == true) saved else null,
                    imageUri = if (type?.startsWith("image") == true) saved else null,
                    audioUri = if (type?.startsWith("audio") == true) saved else null
                )

                isChannel -> sendMessage(
                    senderId = currentUserId,
                    channelUuid = channel?.uuid,
                    videoUri = if (type?.startsWith("video") == true) saved else null,
                    imageUri = if (type?.startsWith("image") == true) saved else null,
                    audioUri = if (type?.startsWith("audio") == true) saved else null
                )

                else -> sendMessage(
                    senderId = currentUserId,
                    receiverId = uuid,
                    videoUri = if (type?.startsWith("video") == true) saved else null,
                    imageUri = if (type?.startsWith("image") == true) saved else null,
                    audioUri = if (type?.startsWith("audio") == true) saved else null
                )
            }
        }
    }
    fun getMessages(
        currentUserId: String,
        userId: String? = null,
        groupUuid: String? = null,
        channelUuid: String? = null
    ): List<Message> {
        return when {
            channelUuid != null -> {
                messages.filter { it.channelUuid == channelUuid }
            }
            groupUuid != null -> {
                messages.filter { it.groupUuid == groupUuid }
            }
            userId != null -> {
                messages.filter {
                    (it.senderId == userId && it.receiverId == currentUserId) ||
                            (it.senderId == currentUserId && it.receiverId == userId)
                }
            }
            else -> emptyList()
        }
    }
    private fun guessTypeFromExtension(path: String): String? {
        return when {
            path.endsWith(".mp4", true) -> "video/mp4"
            path.endsWith(".jpg", true) || path.endsWith(".jpeg", true) -> "image/jpeg"
            path.endsWith(".png", true) -> "image/png"
            path.endsWith(".3gp", true) || path.endsWith(".m4a", true) -> "audio/*"
            else -> null
        }
    }
}