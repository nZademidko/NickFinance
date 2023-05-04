package com.nickfinance.nikfinance.features.main.pager.adapter

import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.baseAdapterDelegate
import com.nickfinance.nikfinance.base.views.builders.ViewSingleLineBuilder
import com.nickfinance.nikfinance.databinding.ItemSingleLineBinding

class SingleLineViewHolder(
    val builder: ViewSingleLineBuilder
) : BaseRowHolder()

fun adapterSingleLineDelegate() =
    baseAdapterDelegate<SingleLineViewHolder, ItemSingleLineBinding>(
        { layoutInflater, root ->
            ItemSingleLineBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            binding.cView.initUI(builder = item.builder)
        }
    }