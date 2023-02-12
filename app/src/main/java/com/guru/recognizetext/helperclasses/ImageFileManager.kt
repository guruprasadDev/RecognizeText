package com.guru.recognizetext.helperclasses

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class ImageFileManager(private val context: Context) {
    private lateinit var tempFile: File
    lateinit var imageUri: Uri

    fun generateImageUri() {
        imageUri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            tempFile
        )
    }

    fun createTempFile() {
        tempFile = File.createTempFile(
            "image",
            ".jpg",
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }
}


