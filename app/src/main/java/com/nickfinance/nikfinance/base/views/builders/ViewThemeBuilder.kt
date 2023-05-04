package com.nickfinance.nikfinance.base.views.builders

class ViewThemeBuilder private constructor() {

    var rootState: RootState = RootState.Transparent()
        private set

    var radiusType: RadiusState = RadiusState.RADIUS_ALL
        private set

    var marginState: MarginState = MarginState.Default
        private set

    var paddingState: PaddingInnerState = PaddingInnerState.Default
        private set

    var amountState: TextState = TextState.Hide
        private set

    var iconState: IconState = IconState.Hide
        private set

    var themeState: TextState = TextState.Hide
        private set

    var isVisible: Boolean = true
        private set

    inner class Builder internal constructor() {

        fun setIconState(value: IconState): Builder {
            this@ViewThemeBuilder.iconState = value
            return this
        }

        fun setThemeState(value: TextState): Builder {
            this@ViewThemeBuilder.themeState = value
            return this
        }

        fun setAmountState(value: TextState): Builder {
            this@ViewThemeBuilder.amountState = value
            return this
        }

        fun setVisible(value: Boolean): Builder {
            this@ViewThemeBuilder.isVisible = value
            return this
        }

        fun setStateRoot(value: RootState): Builder {
            this@ViewThemeBuilder.rootState = value
            return this
        }

        fun setRadiusType(value: RadiusState): Builder {
            this@ViewThemeBuilder.radiusType = value
            return this
        }

        fun setMarginState(value: MarginState): Builder {
            this@ViewThemeBuilder.marginState = value
            return this
        }

        fun setPaddingState(value: PaddingInnerState): Builder {
            this@ViewThemeBuilder.paddingState = value
            return this
        }

        fun build(): ViewThemeBuilder {
            return this@ViewThemeBuilder
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return ViewThemeBuilder().Builder()
        }
    }
}