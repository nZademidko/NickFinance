package com.nickfinance.nikfinance.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpenseWithTheme(
    val expense: Expense,
    val theme: Theme
) : Parcelable