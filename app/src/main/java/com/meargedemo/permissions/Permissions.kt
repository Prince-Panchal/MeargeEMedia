package com.meargedemo.permissions

import VideoHandle.EpEditor
import VideoHandle.EpVideo
import VideoHandle.OnEditorListener
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.googlecode.mp4parser.BasicContainer
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import com.meargedemo.MainActivityVM
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*
import kotlin.collections.ArrayList


object Permissions {

    fun locPermissionCheck(activity: Activity): Boolean {

        if (Build.VERSION.SDK_INT >= 23) {
            val hasReadPermission =
                activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val hasWritePermission =
                activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val hasNetworkStatePermission = activity.checkSelfPermission(Manifest.permission.CAMERA)
            val hasRecordAudio = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO)

            val permissionList = ArrayList<String>()

            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (hasNetworkStatePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA)
            }
            if (hasRecordAudio != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.RECORD_AUDIO)
            }
            if (permissionList.isNotEmpty()) {
                activity.requestPermissions(permissionList.toTypedArray(), 2)
            } else {
                return true
            }
        }
        return false
    }




}