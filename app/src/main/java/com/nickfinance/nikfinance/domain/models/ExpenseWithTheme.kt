package com.nickfinance.nikfinance.domain.models

import com.nickfinance.nikfinance.data.models.Expense
import com.nickfinance.nikfinance.data.models.Theme

data class ExpenseWithTheme(
    val expense: Expense,
    val theme: Theme
)