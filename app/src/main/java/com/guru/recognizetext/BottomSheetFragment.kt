package com.guru.recognizetext

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.guru.recognizetext.databinding.FragmentBottomSheetBinding
import com.guru.recognizetext.utils.Constants
import com.guru.recognizetext.utils.Constants.CAMERA_REQUEST_CODE
import com.guru.recognizetext.utils.Constants.IMAGE
import com.guru.recognizetext.utils.Constants.STORAGE_REQUEST_CODE
import com.guru.recognizetext.utils.showToast

class ImagePickerBottomSheet(val onResult: (Uri) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        initListener()
        return binding.root
    }

    private fun initListener() {
        // Set a click listener for the camera option
        binding.cameraOption.setOnClickListener {
            //pickImageCamera()
            dismiss()
        }

        // Set a click listener for the gallery option
        binding.galleryOption.setOnClickListener {
            pickImageGallery()
        }
    }


    private fun pickImageGallery() {

        galleryActivityResultLauncher.launch(IMAGE)
    }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { uri ->
                onResult(uri)
                dismiss()
            }
        }
//
//
//    private val cameraActivityResultsLauncher =
//        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                binding.imageIv.setImageURI(imageUri)
//            } else {
//                showToast(getString(R.string.cancelled_message))
//            }
//        }
}
