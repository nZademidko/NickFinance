package com.nickfinance.nikfinance.base.adapter.holders

import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.baseAdapterDelegate
import com.nickfinance.nikfinance.base.views.builders.ViewThemeBuilder
import com.nickfinance.nikfinance.databinding.ItemCustomThemeBinding

class ThemeHolder(
    val builder: ViewThemeBuilder
) : BaseRowHolder()

fun adapterThemeDelegate() =
    baseAdapterDelegate<ThemeHolder, ItemCustomThemeBinding>(
        { layoutInflater, root ->
            ItemCustomThemeBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            binding.cView.initUI(builder = item.builder)
        }
    }