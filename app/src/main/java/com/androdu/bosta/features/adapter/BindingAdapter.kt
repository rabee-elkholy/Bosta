package com.androdu.bosta.features.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.androdu.bosta.data.api.model.Photo
import com.github.chrisbanes.photoview.PhotoView

object BindingAdapter {
    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: ImageView, photo: Photo) {
        imageView.load(photo.url) {
            crossfade(200)
        }
    }

    @BindingAdapter("loadImageFromUrl")
    @JvmStatic
    fun loadImageFromUrl(imageView: PhotoView, imageUrl: String?) {
        imageView.load(imageUrl) {
            crossfade(200)
        }
    }

}