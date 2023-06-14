package com.nickfinance.nikfinance.domain.models

import com.nickfinance.nikfinance.base.ThemeIconState
import com.nickfinance.nikfinance.data.database.entity.ThemeDBE

data class AddThemeData(
    val name: String? = null,
    val iconState: ThemeIconState? = null,
    val color: Int? = null
) {

    fun toThemeDBE() = ThemeDBE(
        name = name!!,
        iconId = iconState!!.value,
        color = String.format("#%06X", (0xFFFFFF and color!!))
    )

}