package com.nickfinance.nikfinance.base.views.builders

import android.util.ArrayMap

class ViewChartBuilder private constructor() {

    var textCenterState: TextState = TextState.Hide

    lateinit var data: ArrayMap<String, Pair<Double, Int>>

    var clickState: ((String) -> Unit)? = null

    inner class Builder internal constructor() {

        fun setTextCenterState(state: TextState): Builder {
            this@ViewChartBuilder.textCenterState = state
            return this
        }

        fun setData(data: ArrayMap<String, Pair<Double, Int>>): Builder {
            this@ViewChartBuilder.data = data
            return this
        }

        fun setClickAction(state: (String) -> Unit): Builder {
            this@ViewChartBuilder.clickState = state
            return this
        }

        fun build(): ViewChartBuilder {
            return this@ViewChartBuilder
        }
    }

    companion object {
        fun newBuilder(): ViewChartBuilder.Builder {
            return ViewChartBuilder().Builder()
        }
    }
}