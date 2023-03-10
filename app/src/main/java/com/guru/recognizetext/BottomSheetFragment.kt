package com.guru.recognizetext

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.guru.recognizetext.databinding.FragmentBottomSheetBinding
import com.guru.recognizetext.helperclasses.ImageFileManager
import com.guru.recognizetext.utils.Constants.IMAGE

class ImagePickerBottomSheet(val onResult: (Uri) -> Unit) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var imageHelper: ImageFileManager
    private lateinit var galleryActivityResultLauncher: ActivityResultLauncher<String>
    private lateinit var cameraActivityResultsLauncher: ActivityResultLauncher<Uri>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
                result?.let { uri ->
                    onResult(uri)
                    dismiss()
                }
            }

        cameraActivityResultsLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessfullyCaptured ->
                if (isSuccessfullyCaptured) {
                    onResult(imageHelper.imageUri)
                    dismiss()
                }
            }

        initListener()
        imageHelper = ImageFileManager(requireContext()).also {
            it.createTempFile()
            it.generateImageUri()
        }
    }

    private fun initListener() {
        with(binding) {
            cameraOption.setOnClickListener {
                pickImageCamera()
            }

            galleryOption.setOnClickListener {
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() = galleryActivityResultLauncher.launch(IMAGE)

    private fun pickImageCamera() = cameraActivityResultsLauncher.launch(imageHelper.imageUri)
}
