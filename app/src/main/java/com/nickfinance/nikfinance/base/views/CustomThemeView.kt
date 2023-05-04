package com.nickfinance.nikfinance.base.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.utils.extensions.*

class CustomThemeView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mcvContainer: MaterialCardView
    private val clContainer: LinearLayout
    private val tvTheme: TextView
    private val tvAmount: TextView
    private val ivIcon: ImageView

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_custom_theme, this)
        mcvContainer = findViewById(R.id.mcvContainer)
        clContainer = findViewById(R.id.llContainer)
        tvTheme = findViewById(R.id.tvTheme)
        tvAmount = findViewById(R.id.tvAmount)
        ivIcon = findViewById(R.id.ivIcon)
    }

    fun initUI(builder: ViewThemeBuilder) {
        isVisible = builder.isVisible

        ivIcon.initByState(builder.iconState)
        tvTheme.initByState(state = builder.themeState)
        tvAmount.initByState(state = builder.amountState)

        val paddingState = builder.paddingState
        clContainer.setPadding(
            context.dpToPixSize(paddingState._left.toFloat()),
            context.dpToPixSize(paddingState._top.toFloat()),
            context.dpToPixSize(paddingState._right.toFloat()),
            context.dpToPixSize(paddingState._bottom.toFloat())
        )

        val layoutParams: MarginLayoutParams = when (this.layoutParams) {
            is LayoutParams -> layoutParams as LayoutParams
            is RelativeLayout.LayoutParams -> layoutParams as RelativeLayout.LayoutParams
            is ConstraintLayout.LayoutParams -> layoutParams as ConstraintLayout.LayoutParams
            is RecyclerView.LayoutParams -> layoutParams as RecyclerView.LayoutParams
            else -> layoutParams as LayoutParams
        }
        mcvContainer.setRadii(
            topRightRadius = builder.radiusType.topRightRadius,
            topLeftRadius = builder.radiusType.topLeftRadius,
            bottomRightRadius = builder.radiusType.bottomRightRadius,
            bottomLeftRadius = builder.radiusType.bottomLeftRadius
        )
        layoutParams.topMargin = context.dpToPixSize(builder.marginState._top.toFloat())
        layoutParams.bottomMargin = context.dpToPixSize(builder.marginState._bottom.toFloat())
        layoutParams.leftMargin = context.dpToPixSize(builder.marginState._left.toFloat())
        layoutParams.rightMargin = context.dpToPixSize(builder.marginState._right.toFloat())
        when (val state = builder.rootState) {
            is RootState.Fill -> {
                mcvContainer.setCardBackgroundColor(
                    when (val colorState = state.colorState) {
                        is ColorState.FromIntColor -> colorState.colorId
                        is ColorState.FromResource -> context.getColorCompat(colorState.colorId)
                    }
                )
            }
            is RootState.Transparent -> {
                mcvContainer.setCardBackgroundColor(context.getColorCompat(R.color.transparent))
            }
        }

        when (val clickState = builder.rootState._clickState) {
            is ClickState.Action -> {
                mcvContainer.isClickable = true
                mcvContainer.setOnClickListener { clickState.toAction.invoke() }
            }
            ClickState.None -> {
                mcvContainer.isClickable = false
            }
        }
    }
}