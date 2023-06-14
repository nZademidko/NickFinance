package com.nickfinance.nikfinance.base.adapter.holders

import com.nickfinance.nikfinance.base.adapter.BaseRowHolder
import com.nickfinance.nikfinance.base.adapter.baseAdapterDelegate
import com.nickfinance.nikfinance.databinding.ItemMainListTimeBinding

class TimeHolder(
    val time: String
) : BaseRowHolder()

fun adapterTimeDelegate() =
    baseAdapterDelegate<TimeHolder, ItemMainListTimeBinding>(
        { layoutInflater, root ->
            ItemMainListTimeBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            binding.tvTime.text = item.time
        }
    }

