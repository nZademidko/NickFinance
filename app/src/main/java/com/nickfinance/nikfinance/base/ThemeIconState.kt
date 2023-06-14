package com.nickfinance.nikfinance.base

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.nickfinance.nikfinance.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ThemeIconState(@DrawableRes val resId: Int, val value: String) : Parcelable {
    BUS(resId = R.drawable.ic_theme_bus, value = "bus"),
    FOOD(resId = R.drawable.ic_theme_food, value = "food"),
    CAFE(resId = R.drawable.ic_theme_cafe, value = "cafe"),
    EDUCATION(resId = R.drawable.ic_theme_education, value = "education"),
    LEISURE(resId = R.drawable.ic_theme_leisure, value = "leisure"),
    OTHER(resId = R.drawable.ic_theme_other, value = "other"),
    HOTSPOT(resId = R.drawable.ic_theme_hotspot, value = "hotspot"),
    AIRPLANE(resId = R.drawable.ic_theme_airplane, value = "airplane"),
    BABY(resId = R.drawable.ic_theme_baby, value = "baby")
}