package com.example.lynx.view.media.components

import android.view.ViewGroup
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

fun ReleaseExo(
    playerView: PlayerView?,
    player: ExoPlayer
) {
        playerView?.player = null
        player.stop()
        player.clearMediaItems()
        player.release()
        (playerView?.parent as? ViewGroup)?.removeView(playerView)
}