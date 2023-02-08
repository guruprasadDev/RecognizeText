package com.guru.recognizetext

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.guru.recognizetext.databinding.ActivityMainBinding
import com.guru.recognizetext.helperclasses.CustomDialog
import com.guru.recognizetext.helperclasses.DialogHelper
import com.guru.recognizetext.helperclasses.PermissionHelper
import com.guru.recognizetext.utils.Constants.CAMERA
import com.guru.recognizetext.utils.Constants.CAMERA_REQUEST_CODE
import com.guru.recognizetext.utils.Constants.GALLERY
import com.guru.recognizetext.utils.Constants.IMAGE
import com.guru.recognizetext.utils.Constants.STORAGE_REQUEST_CODE
import com.guru.recognizetext.utils.showToast
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var dialogHelper: DialogHelper
    private lateinit var textRecognizer: TextRecognizer
    private var imageUri: Uri? = null
    private lateinit var imagePickerBottomSheet: ImagePickerBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialogHelper = DialogHelper(this)
        permissionHelper = PermissionHelper(this)

        initListener()
        initTextRecognizer()
        initBottomSheet()
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

    private fun initBottomSheet() {
        imagePickerBottomSheet = ImagePickerBottomSheet(onResult = { imgUri ->
            binding.imageIv.setImageURI(imgUri)
        })
    }

    private fun initTextRecognizer() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private fun recognizeTextFromImage() {
        dialogHelper.alertDialog.setMessage(getString(R.string.preparing_image_message))
        dialogHelper.showAlertDialog()

        try {
            val inputImage = imageUri?.let { imgUri -> InputImage.fromFilePath(this, imgUri) }
            //  dialogHelper.alertDialog.setMessage(getString(R.string.recognize_txt))

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

//    private fun pickImageGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = IMAGE
//        galleryActivityResultLauncher.launch(intent)
//    }
//
//    private val galleryActivityResultLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data
//                imageUri = data?.data
//                binding.imageIv.setImageURI(imageUri)
//            } else {
//                showToast("Cancelled....")
//            }
//        }
//
//    private fun pickImageCamera() {
//        val values = ContentValues()
//        values.put(MediaStore.Images.Media.TITLE, getString(R.string.sample_title_message))
//        values.put(
//            MediaStore.Images.Media.DESCRIPTION,
//            getString(R.string.sample_description_message)
//        )
//
//        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//        cameraActivityResultsLauncher.launch(intent)
//    }
//
//    private val cameraActivityResultsLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                binding.imageIv.setImageURI(imageUri)
//            } else {
//                showToast(getString(R.string.cancelled_message))
//            }
//        }

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
