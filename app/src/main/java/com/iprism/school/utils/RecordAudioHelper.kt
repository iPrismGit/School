package com.iprism.school.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import java.io.File

class RecordAudioHelper (private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    var audioFilePath: String? = null
        private set

    fun startRecording(): String? {
        val audioFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "recording_${System.currentTimeMillis()}.3gp"
        )
        audioFilePath = audioFile.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(audioFilePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            prepare()
            start()
        }
        return audioFilePath
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }
}