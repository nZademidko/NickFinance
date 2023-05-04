package com.nickfinance.nikfinance.base.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Type
import com.nickfinance.nikfinance.base.views.builders.*
import com.nickfinance.nikfinance.utils.extensions.*

class CustomSingleLineView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private val mcvContainer: MaterialCardView
    private val clContainer: ConstraintLayout
    private val ivIconLeft: ImageView
    private val tvTextLeft: TextView
    private val tvTextRight: TextView
    private val ivIconRight: ImageView

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_custom_single_line, this)
        mcvContainer = findViewById(R.id.mcvContainer)
        clContainer = findViewById(R.id.clContainer)
        ivIconLeft = findViewById(R.id.ivIconLeft)
        tvTextLeft = findViewById(R.id.tvTextLeft)
        tvTextRight = findViewById(R.id.tvTextRight)
        ivIconRight = findViewById(R.id.ivIconRight)
    }

    fun initUI(builder: ViewSingleLineBuilder) {
        isVisible = builder.isVisible

        initImageViewByState(targetImageView = ivIconRight, state = builder.iconRightState)
        initImageViewByState(targetImageView = ivIconLeft, state = builder.iconLeftState)

        tvTextLeft.initByState(state = builder.textLeftState)
        tvTextRight.initByState(state = builder.textRightState)

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

    private fun initImageViewByState(targetImageView: ImageView, state: IconState) {
        when (state) {
            IconState.Hide -> {
                targetImageView.isVisible = false
            }
            is IconState.ShowFromRes -> {
                targetImageView.isVisible = true
                targetImageView.loader(
                    Type.FromRes(
                        resId = state.iconId,
                        colorState = state.colorState
                    )
                )
            }
            is IconState.ShowProgress -> {
                targetImageView.isVisible = true
                targetImageView.loader(Type.Progress())
            }
        }
        targetImageView.updateLayoutParams<ConstraintLayout.LayoutParams> {
            when (state._typeOrientation) {
                TypeOrientation.TOP -> {
                    this.topToTop = clContainer.id
                    this.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                }
                TypeOrientation.CENTER -> {
                    this.topToTop = clContainer.id
                    this.bottomToBottom = clContainer.id
                }
                TypeOrientation.BOTTOM -> {
                    this.topToTop = ConstraintLayout.LayoutParams.UNSET
                    this.bottomToBottom = clContainer.id
                }
            }
        }
        when (val clickState = state._clickState) {
            is ClickState.Action -> {
                targetImageView.isClickable = true
                targetImageView.setOnClickListener { clickState.toAction.invoke() }
            }
            ClickState.None -> {
                targetImageView.isClickable = false
            }
        }
    }
}