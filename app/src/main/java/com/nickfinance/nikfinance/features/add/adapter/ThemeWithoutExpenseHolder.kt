package com.nickfinance.nikfinance.features.add.adapter

import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.baseAdapterDelegate
import com.nickfinance.nikfinance.base.views.builders.ViewThemeWithoutExpenseBuilder
import com.nickfinance.nikfinance.databinding.ItemCustomThemeBinding
import com.nickfinance.nikfinance.databinding.ItemCustomThemeWithoutExpenseBinding

class ThemeWithoutExpenseHolder(
    val builder: ViewThemeWithoutExpenseBuilder
) : BaseRowHolder()

fun adapterThemeWithoutExpenseDelegate() =
    baseAdapterDelegate<ThemeWithoutExpenseHolder, ItemCustomThemeWithoutExpenseBinding>(
        { layoutInflater, root ->
            ItemCustomThemeWithoutExpenseBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            binding.cView.initUI(builder = item.builder)
        }
    }