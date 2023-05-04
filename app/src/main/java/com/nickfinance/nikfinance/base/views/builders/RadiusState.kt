package com.nickfinance.nikfinance.base.views.builders

enum class RadiusState(
    val topMargin: Int,
    val bottomMargin: Int,
    val topLeftRadius: Float,
    val topRightRadius: Float,
    val bottomLeftRadius: Float,
    val bottomRightRadius: Float
) {
    RADIUS_TOP(
        topMargin = 4,
        bottomMargin = 0,
        topLeftRadius = 8f,
        topRightRadius = 8f,
        bottomLeftRadius = 0f,
        bottomRightRadius = 0f
    ),
    RADIUS_BOTTOM(
        topMargin = 0,
        bottomMargin = 4,
        topLeftRadius = 0f,
        topRightRadius = 0f,
        bottomLeftRadius = 8f,
        bottomRightRadius = 8f
    ),
    RADIUS_ALL(
        topMargin = 4,
        bottomMargin = 4,
        topLeftRadius = 8f,
        topRightRadius = 8f,
        bottomLeftRadius = 8f,
        bottomRightRadius = 8f
    ),
    RADIUS_NONE(
        topMargin = 0,
        bottomMargin = 0,
        topLeftRadius = 0f,
        topRightRadius = 0f,
        bottomLeftRadius = 0f,
        bottomRightRadius = 0f
    )
}