package com.guru.recognizetext.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import javax.inject.Inject

class ImageFileManager @Inject constructor(private val context: Context) {
    private lateinit var tempFile: File
    lateinit var imageUri: Uri

    fun createTempFile() {
        val uniqueValue = System.currentTimeMillis().toString()
        tempFile = File.createTempFile(
            "image_$uniqueValue",
            ".jpg",
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        generateImageUri()
    }

    private fun generateImageUri() {
        imageUri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            tempFile
        )
    }
}
