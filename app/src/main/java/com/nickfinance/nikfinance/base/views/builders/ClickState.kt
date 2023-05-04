package com.nickfinance.nikfinance.base.views.builders

sealed class ClickState {
    object None : ClickState()
    class Action(val toAction: () -> Unit) : ClickState()
}