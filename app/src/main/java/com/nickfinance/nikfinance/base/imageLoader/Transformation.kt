package com.nickfinance.nikfinance.base.imageLoader

sealed class Transformation {
    object None : Transformation()
    object Circle : Transformation()
    class RoundedAllCorners(val radius_dp: Float) : Transformation()
}
