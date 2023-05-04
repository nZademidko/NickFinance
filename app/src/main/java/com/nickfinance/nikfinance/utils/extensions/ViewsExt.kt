package com.nickfinance.nikfinance.utils.extensions

import android.graphics.Color
import android.os.Build
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.CornerFamily
import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Transformation
import com.nickfinance.nikfinance.base.imageLoader.Type
import com.nickfinance.nikfinance.base.views.builders.IconState
import com.nickfinance.nikfinance.base.views.builders.TextState

fun MaterialCardView.setRadii(
    topLeftRadius: Float,
    topRightRadius: Float,
    bottomLeftRadius: Float,
    bottomRightRadius: Float
) {
    val builder = shapeAppearanceModel
        .toBuilder()
        .setTopLeftCorner(
            CornerFamily.ROUNDED,
            context.dpToPixSize(topLeftRadius).toFloat()
        )
        .setTopRightCorner(
            CornerFamily.ROUNDED,
            context.dpToPixSize(topRightRadius).toFloat()
        )
        .setBottomLeftCorner(
            CornerFamily.ROUNDED,
            context.dpToPixSize(bottomLeftRadius).toFloat()
        )
        .setBottomRightCorner(
            CornerFamily.ROUNDED,
            context.dpToPixSize(bottomRightRadius).toFloat()
        )
        .build()
    shapeAppearanceModel = builder
}

fun ImageView.loader(type: Type) {
    val image = this
    when (type) {
        is Type.FromRes -> {
            image.setImageResource(type.resId)
            when (type.colorState) {
                is ColorState.FromResource -> {
                    image.imageTintList = context.getColorStateListCompat(type.colorState.colorId)
                }

                is ColorState.FromIntColor -> {
                    image.setColorFilter(type.colorState.colorId)
                }

                null -> {}
            }
        }
        is Type.Progress -> {
            val progress = CircularProgressDrawable(context)
            progress.strokeWidth = context.dpToPixSize(type.dpStrokeWidth).toFloat()
            progress.centerRadius = context.dpToPixSize(type.dpRadius).toFloat()
            progress.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                context.getColorCompat(R.color.md_theme_light_primary),
                BlendModeCompat.SRC_IN
            )
            progress.start()
            image.setImageDrawable(progress)
        }
        is Type.FromBitmap -> {
            val loader = context.imageLoader.newBuilder()
            val request = ImageRequest.Builder(context)
                .data(type.bitmap)
                .target(image)
            when (val transformation = type.transformation) {
                Transformation.None -> {
                    request.transformations()
                }
                Transformation.Circle -> {
                    request.transformations(CircleCropTransformation())
                }
                is Transformation.RoundedAllCorners -> {
                    request.transformations(
                        RoundedCornersTransformation(
                            radius = context.dpToPixSize(transformation.radius_dp).toFloat()
                        )
                    )
                }
            }
            loader.build().enqueue(request.build())
        }
    }
}

fun ImageView.initByState(state: IconState) {
    when (state) {
        IconState.Hide -> {
            this.isVisible = false
        }
        is IconState.ShowFromRes -> {
            this.isVisible = true
            this.loader(
                Type.FromRes(
                    resId = state.iconId,
                    colorState = state.colorState
                )
            )
        }
        is IconState.ShowProgress -> {
            this.isVisible = true
            this.loader(Type.Progress())
        }
    }
}

fun TextView.initByState(state: TextState) {
    when (state) {
        TextState.Hide -> {
            this.isVisible = false
        }
        is TextState.Show -> {
            this.isVisible = true

            if (state.spannable == null) {
                this.text = state.value
            } else {
                this.text = state.spannable
                this.movementMethod = LinkMovementMethod.getInstance()
            }

            this.isAllCaps = state.isCaps
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.setTextAppearance(state.styleId)
            } else {
                @Suppress("DEPRECATION") this.setTextAppearance(
                    context,
                    state.styleId
                )
            }
            when (state.colorState) {
                is ColorState.FromResource -> {
                    this.setTextColor(context.getColorCompat(state.colorState.colorId))
                }
                is ColorState.FromIntColor -> {
                    this.setTextColor(state.colorState.colorId)
                }
            }

        }
    }
}