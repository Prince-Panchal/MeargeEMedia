package com.meargedemo

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.meargedemo.common.CommonMethods
import com.meargedemo.common.CommonMethods.mergeVideoEp
import com.meargedemo.permissions.Permissions


class MainActivityVM(val context: Context) : ViewModel() {

    lateinit var fileUri: Uri
    var uriField = ObservableField<String>("")
    var pathOne: ObservableField<String> = ObservableField("")
    var pathTwo: ObservableField<String> = ObservableField("")


    fun onCameraClick() {
        if (Permissions.locPermissionCheck(context as Activity)) {
            try {
                val values = ContentValues()
                fileUri = context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values) as Uri
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
                context.startActivityForResult(intent, 2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun pickGallery() {
        if (Permissions.locPermissionCheck(context as Activity)) {
            try {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                context.startActivityForResult(intent, 6)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun merge() {
        CommonMethods.progessIsShowing(context)
        mergeVideoEp(context, pathOne.get()!!, pathTwo.get()!!, object : OnMerged {
            override fun mergedSuccess(uri: Uri) {
                CommonMethods.dismissProgress()
                uriField.set(uri.toString())
            }

            override fun mergedFailure() {
                CommonMethods.dismissProgress()
                Toast.makeText(context, "Some error occured", Toast.LENGTH_LONG).show()
            }

            override fun mergedProgress(progress: Float) {

            }


        })
    }


    interface OnMerged {
        fun mergedSuccess(parse: Uri)
        fun mergedFailure()
        fun mergedProgress(progress: Float)
    }
}