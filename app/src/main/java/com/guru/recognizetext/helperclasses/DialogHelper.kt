package com.guru.recognizetext.helperclasses

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.core.app.ActivityCompat
import com.guru.recognizetext.R
import javax.inject.Inject

class DialogHelper @Inject constructor(private val context: Context) {
     var alertDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.please_wait_dialog))
        builder.setCancelable(false)
        alertDialog = builder.create()
    }

    fun showAlertDialog() {
        alertDialog.show()
    }

    fun dismissAlertDialog() {
        alertDialog.dismiss()
    }

    companion object {
        fun showPermissionDialog(
            activity: Activity,
            permission: Array<String>,
            requestCode: Int,
            message: String
        ) {
            AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(activity, permission, requestCode)
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }
}
