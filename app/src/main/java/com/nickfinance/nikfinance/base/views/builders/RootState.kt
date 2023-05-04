package com.nickfinance.nikfinance.base.views.builders

import android.graphics.Color
import androidx.annotation.ColorRes
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.imageLoader.ColorState

sealed class RootState(val _clickState: ClickState, val _colorState: ColorState) {
    class Fill(
        val colorState: ColorState = ColorState.FromResource(R.color.gray),
        val clickState: ClickState
    ) : RootState(_clickState = clickState, _colorState = colorState)

    class Transparent(val clickState: ClickState = ClickState.None) :
        RootState(
            _clickState = clickState,
            _colorState = ColorState.FromResource(R.color.transparent)
        )
}