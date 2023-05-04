package com.nickfinance.nikfinance.base.imageLoader

import androidx.annotation.ColorRes

sealed class ColorState() {

    class FromResource(@ColorRes val colorId: Int) : ColorState()

    class FromIntColor(val colorId: Int) : ColorState()
}