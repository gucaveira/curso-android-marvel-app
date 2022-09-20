package com.example.marvelapp.framework.imageLoader

import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {
    fun load(imageView: ImageView, imageUrl: String, @DrawableRes fallback: Int)
}