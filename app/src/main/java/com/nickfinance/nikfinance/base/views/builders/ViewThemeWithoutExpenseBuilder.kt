package com.nickfinance.nikfinance.base.views.builders

class ViewThemeWithoutExpenseBuilder private constructor() {

    var rootState: RootState = RootState.Transparent()
        private set

    var radiusType: RadiusState = RadiusState.RADIUS_ALL
        private set

    var marginState: MarginState = MarginState.Default
        private set

    var paddingState: PaddingInnerState = PaddingInnerState.Default
        private set

    var iconState: IconState = IconState.Hide
        private set

    var themeState: TextState = TextState.Hide
        private set

    var isVisible: Boolean = true
        private set

    inner class Builder internal constructor() {

        fun setIconState(value: IconState): Builder {
            this@ViewThemeWithoutExpenseBuilder.iconState = value
            return this
        }

        fun setThemeState(value: TextState): Builder {
            this@ViewThemeWithoutExpenseBuilder.themeState = value
            return this
        }


        fun setVisible(value: Boolean): Builder {
            this@ViewThemeWithoutExpenseBuilder.isVisible = value
            return this
        }

        fun setStateRoot(value: RootState): Builder {
            this@ViewThemeWithoutExpenseBuilder.rootState = value
            return this
        }

        fun setRadiusType(value: RadiusState): Builder {
            this@ViewThemeWithoutExpenseBuilder.radiusType = value
            return this
        }

        fun setMarginState(value: MarginState): Builder {
            this@ViewThemeWithoutExpenseBuilder.marginState = value
            return this
        }

        fun setPaddingState(value: PaddingInnerState): Builder {
            this@ViewThemeWithoutExpenseBuilder.paddingState = value
            return this
        }

        fun build(): ViewThemeWithoutExpenseBuilder {
            return this@ViewThemeWithoutExpenseBuilder
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return ViewThemeWithoutExpenseBuilder().Builder()
        }
    }
}