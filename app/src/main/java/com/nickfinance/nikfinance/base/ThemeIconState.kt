package com.nickfinance.nikfinance.base

import androidx.annotation.DrawableRes
import com.nickfinance.nikfinance.R

enum class ThemeIconState(@DrawableRes val resId: Int, val value: String) {
    BUS(resId = R.drawable.ic_theme_bus, value = "bus"),
    FOOD(resId = R.drawable.ic_theme_food, value = "food"),
    CAFE(resId = R.drawable.ic_theme_cafe, value = "cafe"),
    EDUCATION(resId = R.drawable.ic_theme_education, value = "education"),
    LEISURE(resId = R.drawable.ic_theme_leisure, value = "leisure"),
    OTHER(resId = R.drawable.ic_theme_other, value = "other")
}