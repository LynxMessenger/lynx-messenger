package com.example.lynx.view.chat.components

import android.media.MediaPlayer
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.lynx.R
import com.example.lynx.model.Channel
import com.example.lynx.model.Message
import com.example.lynx.session.SessionManager
import com.example.lynx.view.media.components.GetImageAspectRatio
import com.example.lynx.view.media.components.GetVideoAspectRatio
import com.example.lynx.view.media.components.ReleaseExo
import com.example.lynx.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MessageBubbleView(
    navController: NavController,
    message: Message,
    isGroup: Boolean = false,
    isChannel: Boolean = false,
    channel: Channel? = null,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    val isCurrentUser = message.senderId == SessionManager.currentUserId
    val sender by userViewModel.getUserFlowByUuid(message.senderId).collectAsState(initial = null)

    val backgroundColor =
        if (isCurrentUser && !isChannel) colorResource(R.color.rich_black) else colorResource(R.color.jet)

    val label = when {
        isGroup -> sender?.userName ?: "error"
        isChannel -> channel?.name ?: "error"
        else -> ""
    }

    val inputFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.getDefault())
    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val timeOnly = try {
        val date = inputFormat.parse(message.timestamp)
        outputFormat.format(date!!)
    } catch (e: Exception) {
        message.timestamp
    }

    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
        horizontalAlignment = if (isCurrentUser && !isChannel) Alignment.End else Alignment.Start
    ) {
        message.audioUri?.let { uriString ->
            val uri = remember(uriString) { uriString.toUri() }

            Text(
                text = label,
                fontSize = 11.sp,
                color = colorResource(R.color.neon_green),
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
            )

            Box(
                modifier = Modifier
                    .widthIn(max = 210.dp)
                    .heightIn(max = 65.dp)
                    .background(backgroundColor, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        try {
                            if (isPlaying) {
                                mediaPlayer.stop()
                                mediaPlayer.reset()
                                isPlaying = false
                            } else {
                                mediaPlayer.setDataSource(context, message.audioUri.toUri())
                                mediaPlayer.prepare()
                                mediaPlayer.start()
                                isPlaying = true

                                mediaPlayer.setOnCompletionListener {
                                    isPlaying = false
                                    mediaPlayer.reset()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Icon(
                            painter = painterResource(if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play),
                            contentDescription = "Play Audio",
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(R.color.neon_green)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Voice message", color = colorResource(R.color.white))
                    }
                    Text(
                        text = timeOnly,
                        color = colorResource(R.color.gray_light),
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontSize = 9.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(top = 15.dp)
                    )
                }
            }
        }
        message.imageUri?.let { uriString ->
            val uri = remember(uriString) { uriString.toUri() }
            val aspectRatio = remember(uri) {
                GetImageAspectRatio(context, uri)
            }

            Text(
                text = label,
                color = colorResource(R.color.neon_green),
                fontSize = 11.sp,
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                modifier = Modifier
                    .padding(start = 6.dp, bottom = 2.dp)
            )

            Box(
                modifier = Modifier
                    .widthIn(max = 450.dp)
                    .heightIn(max = 350.dp)
                    .aspectRatio(aspectRatio)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = uri,
                        builder = { crossfade(true) }
                    ),
                    contentDescription = "Chat image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .matchParentSize()
                        .border(
                            1.dp,
                            color = (colorResource(R.color.neon_green)),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            navController.navigate("ImageViewerScreen/${Uri.encode(uriString)}")
                        }
                )
                Text(
                    text = timeOnly,
                    fontSize = 9.sp,
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .background(color = colorResource(R.color.jet), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        message.videoUri?.let { uriString ->
            val uri = remember(uriString) { uriString.toUri() }

            val lifecycleOwner = LocalLifecycleOwner.current
            val playerViewRef = remember { mutableStateOf<PlayerView?>(null) }

            val exoPlayer = remember(message.videoUri) {
                ExoPlayer.Builder(context).build().apply {
                    volume = 0f
                    setMediaItem(MediaItem.fromUri(message.videoUri.toString()))
                    playWhenReady = true
                    prepare()
                }
            }

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                        ReleaseExo(playerViewRef.value, exoPlayer)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                    ReleaseExo(playerViewRef.value, exoPlayer)
                }
            }

            Text(
                text = label,
                color = colorResource(R.color.neon_green),
                fontSize = 11.sp,
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                modifier = Modifier
                    .padding(start = 6.dp, bottom = 2.dp)
            )

            val context = LocalContext.current
            val aspectRatio = remember(message.videoUri) {
                GetVideoAspectRatio(context, uri)
            }

            Box(
                modifier = Modifier
                    .widthIn(max = 450.dp)
                    .heightIn(max = 350.dp)
                    .aspectRatio(aspectRatio)
                    .border(
                        width = 0.7.dp,
                        color = colorResource(R.color.neon_green),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        navController.navigate("videoPlayer/${Uri.encode(uriString)}")
                    }
            ) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = false
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }.also { playerViewRef.value = it }
                    },
                    modifier = Modifier.matchParentSize()
                )

                Text(
                    text = timeOnly,
                    fontSize = 9.sp,
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .background(color = colorResource(R.color.jet), shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }


        if (!message.text.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .wrapContentWidth()
            ) {
                Column {
                    if(isChannel || isGroup) {
                        Text(
                            text = label,
                            color = colorResource(R.color.neon_green),
                            fontSize = 11.sp,
                            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = message.text,
                            color = colorResource(R.color.white),
                            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .widthIn(max = 240.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = timeOnly,
                            color = colorResource(R.color.gray_light),
                            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                            fontSize = 9.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            textAlign = TextAlign.End,
                        )
                    }
                }
            }
        }
    }
}