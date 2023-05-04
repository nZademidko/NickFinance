package com.nickfinance.nikfinance.utils.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import kotlin.math.ceil

fun Context.dpToPixSize(value: Float): Int {
    return ceil(value * resources.displayMetrics.density).toInt()
}

fun Context.getColorCompat(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

fun Context.getColorStateListCompat(@ColorRes id: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, id)

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? =
    AppCompatResources.getDrawable(this, id)