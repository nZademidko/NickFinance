package com.nickfinance.nikfinance.domain.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Theme(
    val id: Long,
    val name: String,
    val color: Int,
    @DrawableRes val iconId: Int
) : Parcelable