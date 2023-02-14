package com.guru.recognizetext

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.guru.recognizetext.databinding.ActivityMainBinding
import com.guru.recognizetext.helperclasses.DialogHelper
import com.guru.recognizetext.utils.Constants.CAMERA_REQUEST_CODE
import com.guru.recognizetext.utils.Constants.STORAGE_REQUEST_CODE
import com.guru.recognizetext.utils.showToast
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var imagePickerBottomSheet: ImagePickerBottomSheet
    lateinit var dialogHelper: DialogHelper
    private var imageUri: Uri? = null
    @Inject
    lateinit var textRecognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as TRApplication).appComponent.initDi(this) // Inject dependencies
        dialogHelper = DialogHelper(this)
        initListener()
        initBottomSheet()
    }

    private fun initBottomSheet() {
        imagePickerBottomSheet = ImagePickerBottomSheet(onResult = { imgUri ->
            imageUri = imgUri // Set the value of imageUri
            binding.imageIv.setImageURI(imgUri)
        })
    }

    private fun initListener() {
        binding.textRecognizeBtn.setOnClickListener {
            imageUri?.let {
                recognizeTextFromImage()
            } ?: run {
                showToast(getString(R.string.pick_image_first_message))
            }
        }

        binding.imageIv.setOnClickListener {
            imagePickerBottomSheet.show(supportFragmentManager, imagePickerBottomSheet.tag)
        }
    }

    private fun recognizeTextFromImage() {
        dialogHelper.alertDialog.setMessage(getString(R.string.preparing_image_message))
        dialogHelper.showAlertDialog()

        try {
            val inputImage = imageUri?.let { imgUri -> InputImage.fromFilePath(this, imgUri) }
            dialogHelper.alertDialog.setMessage(getString(R.string.recognize_txt))

            inputImage?.let { image ->
                textRecognizer.process(image)
                    .addOnSuccessListener { recognizedText ->
                        dialogHelper.dismissAlertDialog()
                        binding.recognizedTextEt.setText(recognizedText.text)
                    }
                    .addOnFailureListener { e ->
                        dialogHelper.dismissAlertDialog()
                        showToast("Failed to recognize text due to ${e.message}")
                    }
            }

        } catch (e: Exception) {
            dialogHelper.dismissAlertDialog()
            showToast("Filed to prepare timage due to ${e.message}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        //pickImageCamera()
                    } else {
                        showToast(getString(R.string.camera_permission))
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (storageAccepted) {
                        //pickImageGallery()
                    } else {
                        showToast(getString(R.string.storage_permission))
                    }
                }
            }
        }
    }
}
