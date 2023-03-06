package com.nickfinance.nikfinance.domain.repositories.impl

import com.nickfinance.nikfinance.data.database.ExpensesDao
import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import com.nickfinance.nikfinance.data.models.Theme
import com.nickfinance.nikfinance.domain.models.AddExpenseData
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val database: ExpensesDao
) : MainRepository {

    override fun getExpensesWithThemes(): Flow<List<ExpenseWithTheme>> =
        database.getAllExpensesWithThemes()
            .map { list -> list.map { it.toExpenseWithTheme() } }

    override fun getThemes(): Flow<List<Theme>> =
        database.getThemes()
            .map { list -> list.map { it.toTheme() } }

    override suspend fun saveExpense(data: AddExpenseData) {
        database.createExpense(data.toExpenseDBE())
    }
}