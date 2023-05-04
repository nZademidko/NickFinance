package com.nickfinance.nikfinance.base.views.builders

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Transformation

sealed class IconState(val _typeOrientation: TypeOrientation, val _clickState: ClickState) {
    class ShowFromRes(
        @DrawableRes val iconId: Int,
        val colorState: ColorState? = null,
        val typeOrientation: TypeOrientation = TypeOrientation.CENTER,
        val transformation: Transformation = Transformation.None,
        val clickState: ClickState = ClickState.None
    ) : IconState(_typeOrientation = typeOrientation, _clickState = clickState)

    class ShowProgress(
        val dpRadius: Float = 4f,
        val typeOrientation: TypeOrientation = TypeOrientation.CENTER
    ) : IconState(_typeOrientation = typeOrientation, _clickState = ClickState.None)

    object Hide :
        IconState(_typeOrientation = TypeOrientation.CENTER, _clickState = ClickState.None)
}