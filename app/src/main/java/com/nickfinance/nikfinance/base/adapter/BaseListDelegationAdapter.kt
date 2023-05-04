package com.nickfinance.nikfinance.base.adapter

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

open class BaseListDelegationAdapter<T : Any> : ListDelegationAdapter<List<T>> {

    constructor() : super()

    constructor(delegatesManager: AdapterDelegatesManager<List<T>>) : super(delegatesManager)

    constructor(vararg delegates: AdapterDelegate<List<T>>) : super(*delegates)

    fun update(items: List<T>) {
        this.items = items
        this.notifyDataSetChanged()
    }
}