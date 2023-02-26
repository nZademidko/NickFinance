package com.nickfinance.nikfinance.domain.repositories.abstractions

import com.nickfinance.nikfinance.data.models.Theme
import com.nickfinance.nikfinance.domain.models.AddExpenseData
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getExpensesWithThemes(): Flow<List<ExpenseWithTheme>>

    suspend fun getThemes(): List<Theme>

    suspend fun saveExpense(data: AddExpenseData)
}