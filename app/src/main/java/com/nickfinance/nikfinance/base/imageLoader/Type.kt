package com.nickfinance.nikfinance.base.imageLoader

import android.graphics.Bitmap
import androidx.annotation.DrawableRes

sealed class Type {
    class FromRes(@DrawableRes val resId: Int, val colorState: ColorState? = null) : Type()

    class FromBitmap(
        val bitmap: Bitmap,
        val transformation: Transformation = Transformation.None
    ) : Type()

    class Progress(val dpRadius: Float = 4f, val dpStrokeWidth: Float = 1f) : Type()
}