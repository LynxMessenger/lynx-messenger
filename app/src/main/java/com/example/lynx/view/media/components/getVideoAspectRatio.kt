package com.example.lynx.view.media.components

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri

fun GetVideoAspectRatio(context: Context, uri: Uri): Float {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)

        val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toFloatOrNull()
        val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toFloatOrNull()

        if (width != null && height != null && height != 0f) {
            width / height
        } else {
            16f / 9f // fallback
        }
    } catch (e: Exception) {
        16f / 9f // fallback
    } finally {
        retriever.release()
    }
}