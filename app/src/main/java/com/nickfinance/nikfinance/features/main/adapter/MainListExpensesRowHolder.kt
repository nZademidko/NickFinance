package com.nickfinance.nikfinance.features.main.adapter

import com.nickfinance.nikfinance.base.BaseRowHolder
import com.nickfinance.nikfinance.base.baseAdapterDelegate
import com.nickfinance.nikfinance.databinding.ItemMainListExpenseBinding
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.features.add.adapter.ThemesRowHolder

class MainListExpensesRowHolder(
    val expenseWithTheme: ExpenseWithTheme,
    val toSelect: (() -> Unit)? = null
) : BaseRowHolder()

fun adapterMainListExpensesDelegate() =
    baseAdapterDelegate<MainListExpensesRowHolder, ItemMainListExpenseBinding>(
        { layoutInflater, root ->
            ItemMainListExpenseBinding.inflate(layoutInflater, root, false)
        }) {
        bind {
            val data = item.expenseWithTheme
            binding.tvTheme.text = data.theme.name
            binding.tvAmount.text = data.expense.amount.toString()
        }
    }