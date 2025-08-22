package com.example.lynx.view.media

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.lynx.R
import com.example.lynx.view.media.components.GetVideoAspectRatio
import com.example.lynx.view.media.components.ReleaseExo

@Composable
fun VideoPlayerScreen(
    navController: NavController,
    uri: Uri
) {
    val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerViewRef = remember { mutableStateOf<PlayerView?>(null) }

    val aspectRatio = remember(uri) {
        GetVideoAspectRatio(context, uri)
    }

    val exoPlayer = remember(uri) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
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

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.black))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.statusBarsPadding())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_backarrow),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp)
                            .clickable { navController.popBackStack() },
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Video",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 36.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.neon_green),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topPadding, bottom = 35.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    AndroidView(
                        factory = {
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = true
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }.also { playerViewRef.value = it }
                        },
                        modifier = Modifier

                            .fillMaxWidth()
                            .aspectRatio(aspectRatio)
                            .align(Alignment.Center)
                            .padding(top = topPadding, bottom = 35.dp)
                    )
                }
            }
        }
    }
}