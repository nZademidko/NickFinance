package com.nickfinance.nikfinance.base

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate

open class BaseRowHolderAdapter(vararg delegates: AdapterDelegate<List<BaseRowHolder>>) :
    BaseListDelegationAdapter<BaseRowHolder>(*delegates), RowHolderAdapter {

    fun addDelegates(delegates: List<AdapterDelegate<List<BaseRowHolder>>>) {
        delegates.forEach { delegate -> this.delegatesManager.addDelegate(delegate) }
    }

    override fun getItemId(position: Int): Long =
        if (hasStableIds()) {
            items!![position]._id ?: 0
        } else {
            super.getItemId(position)
        }
}