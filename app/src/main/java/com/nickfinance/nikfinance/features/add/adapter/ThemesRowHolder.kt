package com.nickfinance.nikfinance.features.add.adapter

import com.nickfinance.nikfinance.R
import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.baseAdapterDelegate
import com.nickfinance.nikfinance.data.models.Theme
import com.nickfinance.nikfinance.databinding.ItemThemeBinding
import com.nickfinance.nikfinance.utils.extensions.getColorStateListCompat

class ThemesRowHolder(
    val theme: Theme,
    val isSelected: Boolean,
    val toSelect: (() -> Unit)? = null
) : BaseRowHolder()

fun adapterThemesDelegate() =
    baseAdapterDelegate<ThemesRowHolder, ItemThemeBinding>(
        { layoutInflater, root ->
            ItemThemeBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            val data = item.theme
            binding.tvTheme.text = data.name
            if (item.isSelected) {
                binding.cTheme.setCardBackgroundColor(
                    context.getColorStateListCompat(R.color.md_theme_dark_primaryContainer)
                )
                binding.tvTheme.setTextColor(context.getColorStateListCompat(R.color.white))
            } else {
                binding.cTheme.setCardBackgroundColor(
                    context.getColorStateListCompat(R.color.white)
                )
                binding.tvTheme.setTextColor(
                    context.getColorStateListCompat(R.color.md_theme_dark_primaryContainer)
                )
            }
            binding.root.setOnClickListener {
                item.toSelect?.invoke()
            }
        }
    }