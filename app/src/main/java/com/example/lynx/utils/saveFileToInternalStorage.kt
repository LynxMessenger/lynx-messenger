package com.example.lynx.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
interface MediaStorage {
    fun saveImage(context: Context, uri: Uri): Uri?
    fun saveVideo(context: Context, uri: Uri): Uri?
    fun saveAudio(context: Context, uri: Uri): Uri?
}

class RealMediaStorage : MediaStorage {
    override fun saveImage(context: Context, uri: Uri): Uri? {
        return saveFileToInternalStorage(context, uri, "chat_images", "IMG", "jpg")
    }
    override fun saveVideo(context: Context, uri: Uri): Uri? {
        return saveFileToInternalStorage(context, uri, "chat_videos", "VID", "mp4")
    }
    override fun saveAudio(context: Context, uri: Uri): Uri? {
        return saveFileToInternalStorage(context, uri, "chat_audios", "AUD", "m4a")
    }
}

class FakeMediaStorage : MediaStorage {
    override fun saveImage(context: Context, uri: Uri): Uri? =
        Uri.parse("file://mock/chat_images/test.jpg")

    override fun saveVideo(context: Context, uri: Uri): Uri? =
        Uri.parse("file://mock/chat_videos/test.mp4")

    override fun saveAudio(context: Context, uri: Uri): Uri? =
        Uri.parse("file://mock/chat_audios/test.m4a")
}
fun saveFileToInternalStorage(
    context: Context,
    uri: Uri,
    folderName: String,
    prefix: String,
    extension: String
): Uri? {
    return try {
        val storageDir = File(context.filesDir, folderName).apply {
            if (!exists()) mkdirs()
        }

        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.getDefault()).format(Date())
        val fileName = "${prefix}_$timeStamp.$extension"
        val destFile = File(storageDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(destFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        Uri.fromFile(destFile)
    } catch (e: Exception) {
        Log.e("FileSave", "Error saving file", e)
        null
    }
}