package com.nickfinance.nikfinance.data.models

import androidx.annotation.DrawableRes

data class Theme(
    val id: Long,
    val name: String,
    val color: Int,
    @DrawableRes val iconId: Int
)