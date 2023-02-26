package com.nickfinance.nikfinance.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme

data class ExpenseWithThemeDBE(
    @Embedded val expenseDBE: ExpenseDBE,
    @Relation(
        parentColumn = "theme_id",
        entityColumn = "id"
    )
    val themeDBE: ThemeDBE
) {

    fun toExpenseWithTheme() =
        ExpenseWithTheme(expense = expenseDBE.toExpense(), theme = themeDBE.toTheme())

}