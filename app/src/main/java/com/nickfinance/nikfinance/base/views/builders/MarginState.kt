package com.nickfinance.nikfinance.base.views.builders

sealed class MarginState(val _top: Int, val _bottom: Int, val _left: Int, val _right: Int) {
    object Default : MarginState(_top = 4, _bottom = 4, _right = 16, _left = 16)
    class Custom(top: Int, bottom: Int, left: Int, right: Int) : MarginState(_top = top, _bottom = bottom, _right = right, _left = left)
    object None : MarginState(_top = 0, _bottom = 0, _right = 0, _left = 0)
}