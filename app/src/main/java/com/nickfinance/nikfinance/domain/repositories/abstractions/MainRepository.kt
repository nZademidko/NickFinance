package com.nickfinance.nikfinance.domain.repositories.abstractions

import com.nickfinance.nikfinance.domain.models.Expense
import com.nickfinance.nikfinance.domain.models.Theme
import com.nickfinance.nikfinance.domain.models.AddExpenseData
import com.nickfinance.nikfinance.domain.models.AddThemeData
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.models.FilterData
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getExpensesWithThemes(filters: FilterData): Flow<List<ExpenseWithTheme>>

    suspend fun getThemes(): Flow<List<Theme>>

    suspend fun updateExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)

    suspend fun saveExpense(data: AddExpenseData)

    suspend fun saveTheme(data: AddThemeData)
}