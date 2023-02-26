package com.nickfinance.nikfinance.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.AdapterDelegateViewBindingViewHolder
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

abstract class BaseRowHolder(
    val _id: Long? = null
)

inline fun <reified I : BaseRowHolder, V : ViewBinding> baseAdapterDelegate(
    noinline viewBinding: (LayoutInflater, ViewGroup) -> V,
    noinline block: AdapterDelegateViewBindingViewHolder<I, V>.() -> Unit
): AdapterDelegate<List<BaseRowHolder>> {
    val newBlock: AdapterDelegateViewBindingViewHolder<I, V>.() -> Unit = {
        block.invoke(this)
    }
    return adapterDelegateViewBinding(viewBinding = viewBinding, block = newBlock)
}