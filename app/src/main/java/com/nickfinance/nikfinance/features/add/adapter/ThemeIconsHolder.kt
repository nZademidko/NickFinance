package com.nickfinance.nikfinance.features.add.adapter

import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.ThemeIconState
import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.baseAdapterDelegate
import com.nickfinance.nikfinance.base.imageLoader.ColorState
import com.nickfinance.nikfinance.base.imageLoader.Type
import com.nickfinance.nikfinance.databinding.ItemThemeIconBinding
import com.nickfinance.nikfinance.utils.extensions.loader

class ThemeIconsHolder(
    val iconState: ThemeIconState,
    val selected: Boolean = false,
    val onClicked: () -> Unit
) : BaseRowHolder()

fun adapterThemeIconsDelegate() =
    baseAdapterDelegate<ThemeIconsHolder, ItemThemeIconBinding>(
        { layoutInflater, root ->
            ItemThemeIconBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            binding.ivIcon.loader(
                Type.FromRes(
                    resId = item.iconState.resId,
                    colorState =
                    if (item.selected) ColorState.FromResource(R.color.md_theme_light_primary)
                    else ColorState.FromResource(R.color.md_theme_light_tertiary)
                )
            )

            binding.root.setCardBackgroundColor(
                if (item.selected) context.getColorStateList(R.color.md_theme_light_primaryContainer)
                else context.getColorStateList(R.color.md_theme_light_tertiaryContainer)
            )

            binding.root.setOnClickListener {
                item.onClicked.invoke()
            }
        }
    }