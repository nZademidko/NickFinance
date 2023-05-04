package com.nickfinance.nikfinance.base.views.builders

import android.text.SpannableString
import androidx.annotation.StyleRes
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.imageLoader.ColorState

sealed class TextState {
    class Show(
        val value: String = "",
        val colorState: ColorState = ColorState.FromResource(R.color.black),
        @StyleRes val styleId: Int = R.style.TextAppearance_App_Body_B1_text_reg_16,
        val isCaps: Boolean = false,
        val spannable: SpannableString? = null
    ) : TextState()

    object Hide : TextState()
}