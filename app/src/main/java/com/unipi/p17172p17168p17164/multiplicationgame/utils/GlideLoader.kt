package com.unipi.p17172p17168p17164.multiplicationgame.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.unipi.p17172p17168p17164.multiplicationgame.R
import java.io.IOException

/**
 * A custom object to create a common functions for Glide which can be used in whole application.
 */
@GlideModule
class GlideLoader(val context: Context) : AppGlideModule() {

    fun loadIcon(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // Uri or URL of the image
                .centerInside() // Scale type of the image.
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.apply{ RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(
                ObjectKey(System.currentTimeMillis().toShort())
            )
        }
    }
}