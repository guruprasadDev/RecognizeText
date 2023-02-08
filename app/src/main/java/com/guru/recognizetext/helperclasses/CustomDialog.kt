package com.guru.recognizetext.helperclasses

import android.app.Activity
import android.app.AlertDialog
import androidx.core.app.ActivityCompat

class CustomDialog {
    private lateinit var dialogTitle: String
    private var isCancelable: Boolean = false

    class Builder() {
        private var title: String = ""
        private var cancelable: Boolean = false

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun build(): CustomDialog {
            return CustomDialog().apply {
                this.dialogTitle = title
                this.isCancelable = cancelable
            }
        }
    }

    class PermissionDialogBuilder(private val activity: Activity, private val message: String) {
        private var positiveButtonText: String = "OK"
        private var negativeButtonText: String = "Cancel"
        private var permission: Array<String>? = null
        private var requestCode: Int = 0

        fun setPositiveButtonText(text: String): PermissionDialogBuilder {
            this.positiveButtonText = text
            return this
        }

        fun setNegativeButtonText(text: String): PermissionDialogBuilder {
            this.negativeButtonText = text
            return this
        }

        fun setPermission(permission: Array<String>): PermissionDialogBuilder {
            this.permission = permission
            return this
        }

        fun setRequestCode(requestCode: Int): PermissionDialogBuilder {
            this.requestCode = requestCode
            return this
        }

        fun build() {
            AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { dialog, which ->
                    permission?.let {
                        ActivityCompat.requestPermissions(activity, it, requestCode)
                    }
                }
                .setNegativeButton(negativeButtonText, null)
                .create()
                .show()
        }
    }

    companion object {
        fun showPermissionDialog(activity: Activity, message: String, configure: (PermissionDialogBuilder.() -> Unit)? = null) {
            PermissionDialogBuilder(activity, message).apply {
                configure?.invoke(this)
            }.build()
        }
    }
}
