package com.example.lynx.view.chat.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.lynx.R
import com.example.lynx.viewmodel.MessageViewModel
import com.example.lynx.model.Channel
import com.example.lynx.session.SessionManager
import com.example.lynx.utils.AudioRecorder
import java.io.File

@OptIn(UnstableApi::class)
@Composable
fun MessageInputView(
    inputText: MutableState<String>,
    uuid: String,
    isGroup: Boolean = false,
    isChannel: Boolean = false,
    channel: Channel? = null,
    messageViewModel: MessageViewModel
) {
    val context = LocalContext.current

    val isRecording = remember { mutableStateOf(false) }
    val audioRecorder = remember { AudioRecorder(context) }
    var recordedFilePath by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            recordedFilePath = audioRecorder.startRecording()
            isRecording.value = true
        } else {
            Toast.makeText(context, "Разрешение на микрофон не предоставлено", Toast.LENGTH_SHORT)
                .show()
        }
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri ->
        uri?.let {
            messageViewModel.sendMessageWithMedia(
                context = context,
                uri = it,
                isGroup = isGroup,
                isChannel = isChannel,
                uuid = uuid,
                channel = channel
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(colorResource(R.color.dark_blue)),
        contentAlignment = Alignment.BottomStart
    ) {
        when {
            isChannel && channel?.ownerId != SessionManager.currentUserId -> {
                Text(
                    text = "Notification: ON",
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 10.dp)
                )
            }

            isRecording.value -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Recording audio...",
                        color = colorResource(R.color.neon_green),
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = "Stop recording",
                        tint = colorResource(R.color.neon_green),
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .size(24.dp)
                            .clickable {
                                val path = audioRecorder.stopRecording()
                                isRecording.value = false
                                Log.d("MessageInputView", "Recorded file path from stopRecording = $path")
                                path.let {
                                    val fileUri = File(it).toUri()
                                    messageViewModel.sendMessageWithMedia(
                                        context = context,
                                        uri = fileUri,
                                        isGroup = isGroup,
                                        isChannel = isChannel,
                                        uuid = uuid,
                                        channel = channel
                                    )
                                }
                            }
                    )
                }
            }

            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_galerypeek),
                        contentDescription = "Choose image or video",
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .size(20.dp)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(PickVisualMedia.ImageAndVideo)
                                )
                            }
                    )

                    TextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        placeholder = {
                            Text(
                                text = "Type your message...",
                                color = colorResource(R.color.white),
                                fontFamily = FontFamily(Font(R.font.robotomonovariable))
                            )
                        },
                        singleLine = false,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedTextColor = colorResource(R.color.white),
                            unfocusedTextColor = colorResource(R.color.white),
                            cursorColor = colorResource(R.color.white)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    if (inputText.value.isBlank()) {
                        Image(
                            painter = painterResource(R.drawable.ic_microphone),
                            contentDescription = "send voice",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .size(24.dp)
                                .clickable {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.RECORD_AUDIO
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        audioRecorder.startRecording()
                                        isRecording.value = true
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_send),
                            contentDescription = "send message",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .size(20.dp)
                                .clickable {
                                    val text = inputText.value.trim()
                                    if (text.isNotEmpty()) {
                                        when {
                                            isGroup -> messageViewModel.sendMessage(
                                                senderId = SessionManager.currentUserId.toString(),
                                                groupUuid = uuid,
                                                text = text,
                                            )

                                            isChannel -> messageViewModel.sendMessage(
                                                senderId = SessionManager.currentUserId.toString(),
                                                channelUuid = channel?.uuid,
                                                text = text,
                                            )

                                            else -> messageViewModel.sendMessage(
                                                senderId = SessionManager.currentUserId.toString(),
                                                receiverId = uuid,
                                                text = text,
                                            )
                                        }
                                        inputText.value = ""
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}