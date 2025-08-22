package com.example.lynx.view.media.components

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri

fun GetImageAspectRatio(context: Context, uri: Uri): Float {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        val width = options.outWidth
        val height = options.outHeight

        if (width > 0 && height > 0) {
            width.toFloat() / height.toFloat()
        } else {
            1f
        }
    } catch (e: Exception) {
        1f
    }
}