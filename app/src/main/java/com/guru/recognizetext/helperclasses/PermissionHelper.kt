package com.guru.recognizetext.helperclasses

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.guru.recognizetext.R
import com.guru.recognizetext.helperclasses.DialogHelper.Companion.showPermissionDialog
import com.guru.recognizetext.utils.Constants.CAMERA_REQUEST_CODE
import com.guru.recognizetext.utils.Constants.STORAGE_REQUEST_CODE

class PermissionHelper(private var activity: Activity) {

    private var cameraPermission: Array<String> =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private var storagePermission: Array<String> =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun isCameraPermissionsGranted(): Boolean {
        return checkPermissionGranted(
            Manifest.permission.CAMERA,
        )
    }

    fun checkCameraAndStoragePermissions(): Boolean {
        return checkPermissionGranted(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    fun storagePermissionGranted(): Boolean {
        return checkPermissionGranted(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun checkPermissionGranted(vararg permissions: String): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission(
        permission: Array<String>,
        requestCode: Int,
        message: Int,
        permissionString: String,
        isPermissionAccepted: Boolean
    ) {
        if (!isPermissionAccepted) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permissionString
                )
            ) {
                showPermissionDialog(activity, permission, requestCode, activity.getString(message))
            } else {
                ActivityCompat.requestPermissions(activity, permission, requestCode)
            }
        }
    }

    fun requestCameraPermission() {
        requestPermission(
            cameraPermission,
            CAMERA_REQUEST_CODE,
            R.string.camera_access,
            Manifest.permission.CAMERA,
            isCameraPermissionsGranted()
        )
    }

    fun requestStoragePermission() {
        requestPermission(
            storagePermission,
            STORAGE_REQUEST_CODE,
            R.string.storage_access,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            storagePermissionGranted()
        )
    }
}
