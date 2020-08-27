package com.meargedemo.common

import VideoHandle.EpEditor
import VideoHandle.EpVideo
import VideoHandle.OnEditorListener
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import com.googlecode.mp4parser.BasicContainer
import com.googlecode.mp4parser.authoring.Movie
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.AppendTrack
import com.meargedemo.MainActivityVM
import com.meargedemo.R
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.util.*
import kotlin.collections.ArrayList

object CommonMethods {
    fun mergeVideoEp(
        context: Context,
        firstVideoPath: String,
        secondVideoPath: String,
        onMerged: MainActivityVM.OnMerged
    ) {

        val dir = context.filesDir.absolutePath + "/princeTask/"
        val outputFile = File(dir + "merged_video.mp4")
        try {
            if (!outputFile.exists()) {
                File(dir).mkdirs()     // make sure to call mkdirs() when creating new directory
                outputFile.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val epVideos = ArrayList<EpVideo>()
        epVideos.add(EpVideo(firstVideoPath)) // Video 1 Example
        epVideos.add(EpVideo(secondVideoPath)) // Video 2 Exmaple
        val outputOption = EpEditor.OutputOption(outputFile.path) //Output
        outputOption.setWidth(720) // output video width, default 480
        outputOption.setHeight(1280)
        outputOption.frameRate = 25 // output video frame rate, default 30

        EpEditor.merge(epVideos, outputOption, object : OnEditorListener {
            override fun onSuccess() {
                onMerged.mergedSuccess(Uri.parse(outputFile.path))
//                playVideo(Uri.parse(outputFile.path), context)

                Log.e("OutPutOption", outputOption.toString())

            }

            override fun onFailure() {
                onMerged.mergedFailure()

            }


            override fun onProgress(progress: Float) {
                onMerged.mergedProgress(progress)
                Log.d("Progress", "$progress")
            }

        })
    }


    fun appendTwoVideos(firstVideoPath: String, secondVideoPath: String): String? {
        try {
            val inMovies: Array<Movie?> = arrayOfNulls<Movie>(2)

            Log.e("first====>", firstVideoPath)
            Log.e("second====>", secondVideoPath)

            inMovies[0] = MovieCreator.build(secondVideoPath)
            inMovies[1] = MovieCreator.build(firstVideoPath)

            val videoTracks: MutableList<Track> = LinkedList()
            val audioTracks: MutableList<Track> = LinkedList()
            for (m in inMovies) {
                for (t in m!!.tracks) {
                    if (t.handler == "soun") {
                        audioTracks.add(t)
                    }
                    if (t.handler == "vide") {
                        videoTracks.add(t)
                    }
                }
            }
            val result = Movie()
            if (audioTracks.size > 0) {
                result.addTrack(
                    AppendTrack(
                        *audioTracks
                            .toTypedArray()
                    )
                )
            }
            if (videoTracks.size > 0) {
                result.addTrack(
                    AppendTrack(
                        *videoTracks
                            .toTypedArray()
                    )
                )
            }
            val out = DefaultMp4Builder().build(result) as BasicContainer
            val fc: FileChannel = RandomAccessFile(
                Environment.getExternalStorageDirectory().toString() + "/apptunix.mp4", "rw"
            ).channel
            out.writeContainer(fc)
            fc.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var mFileName: String = Environment.getExternalStorageDirectory().absolutePath
        mFileName += "/apptunix.mp4"
        return mFileName
    }

    fun getPath(uri: Uri, context: Context): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor =
            (context as Activity).contentResolver.query(uri, projection, null, null, null)!!
        return run {
            val indeexx: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(indeexx)
        }
    }


    private var progressAlert: AlertDialog? = null

    fun progessIsShowing(context: Context) {
        var customAlertBuilder = AlertDialog.Builder(context)
        val customAlertView = LayoutInflater.from(context).inflate(R.layout.progressbar, null)
        customAlertBuilder.setView(customAlertView)


        progressAlert = customAlertBuilder.create()
        progressAlert!!.show()
        progressAlert!!.setCanceledOnTouchOutside(false)
        progressAlert!!.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    fun dismissProgress() {
        if (progressAlert != null && progressAlert!!.isShowing) {
            progressAlert!!.dismiss()
        }
    }


}