package com.example.lynx.utils

import android.content.Context
import android.media.MediaRecorder
import java.io.File

class AudioRecorder(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var outputFilePath: String = ""

    fun startRecording(): String {
        val file = File(context.cacheDir, "audio_message_${System.currentTimeMillis()}.3gp")
        outputFilePath = file.absolutePath

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFile(outputFilePath)
            prepare()
            start()
        }

        return outputFilePath
    }

    fun stopRecording(): String {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        return outputFilePath
    }
}