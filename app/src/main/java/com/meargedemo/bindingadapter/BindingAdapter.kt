package com.meargedemo.bindingadapter

import android.net.Uri
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField

object BindingAdapter {

    @BindingAdapter(value = ["setVideo"], requireAll = false)
    @JvmStatic
    fun setVideo(videoView: VideoView, uri: ObservableField<String>) {
        if (uri.get()!!.isNotEmpty()){
            videoView.setVideoURI(Uri.parse(uri.get()))
            videoView.start()
        }
    }

}