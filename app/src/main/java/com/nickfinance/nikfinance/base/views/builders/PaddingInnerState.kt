package com.nickfinance.nikfinance.base.views.builders

sealed class PaddingInnerState(val _left: Int, val _top: Int, val _right: Int, val _bottom: Int) {
    object Default : PaddingInnerState(_left = 16, _top = 14, _right = 16, _bottom = 14)
    object DefaultNew : PaddingInnerState(_left = 16, _top = 12, _right = 16, _bottom = 12)
    class Custom(val leftInDp: Int, val topInDp: Int, val rightInDp: Int, val bottomInDp: Int) :
        PaddingInnerState(
            _left = leftInDp,
            _top = topInDp,
            _right = rightInDp,
            _bottom = bottomInDp
        )
}
