package com.nickfinance.nikfinance.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorStateListCompat(@ColorRes id: Int): ColorStateList? =
    ContextCompat.getColorStateList(this, id)
