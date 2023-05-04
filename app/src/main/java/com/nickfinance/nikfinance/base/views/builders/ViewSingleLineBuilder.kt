package com.nickfinance.nikfinance.base.views.builders

class ViewSingleLineBuilder private constructor() {

    var rootState: RootState = RootState.Transparent()
        private set

    var radiusType: RadiusState = RadiusState.RADIUS_ALL
        private set

    var marginState: MarginState = MarginState.Default
        private set

    var paddingState: PaddingInnerState = PaddingInnerState.Default
        private set

    var iconLeftState: IconState = IconState.Hide
        private set

    var textLeftState: TextState = TextState.Hide
        private set

    var iconRightState: IconState = IconState.Hide
        private set

    var textRightState: TextState = TextState.Hide
        private set

    var titleState: TextState = TextState.Hide
        private set

    var commentState: TextState = TextState.Hide
        private set

    var isVisible: Boolean = true
        private set

    inner class Builder internal constructor() {

        fun setVisible(value: Boolean): Builder {
            this@ViewSingleLineBuilder.isVisible = value
            return this
        }

        fun setStateRoot(value: RootState): Builder {
            this@ViewSingleLineBuilder.rootState = value
            return this
        }

        fun setStateIconLeft(value: IconState): Builder {
            this@ViewSingleLineBuilder.iconLeftState = value
            return this
        }

        fun setStateTextLeft(value: TextState): Builder {
            this@ViewSingleLineBuilder.textLeftState = value
            return this
        }

        fun setStateIconRight(value: IconState): Builder {
            this@ViewSingleLineBuilder.iconRightState = value
            return this
        }

        fun setStateTextRight(value: TextState): Builder {
            this@ViewSingleLineBuilder.textRightState = value
            return this
        }

        fun setRadiusType(value: RadiusState): Builder {
            this@ViewSingleLineBuilder.radiusType = value
            return this
        }

        fun setMarginState(value: MarginState): Builder {
            this@ViewSingleLineBuilder.marginState = value
            return this
        }

        fun setPaddingState(value: PaddingInnerState): Builder {
            this@ViewSingleLineBuilder.paddingState = value
            return this
        }

        fun build(): ViewSingleLineBuilder {
            return this@ViewSingleLineBuilder
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return ViewSingleLineBuilder().Builder()
        }
    }
}