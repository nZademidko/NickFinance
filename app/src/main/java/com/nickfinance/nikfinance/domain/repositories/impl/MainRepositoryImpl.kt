package com.nickfinance.nikfinance.domain.repositories.impl

import com.nickfinance.nikfinance.data.database.ExpensesDao
import com.nickfinance.nikfinance.domain.models.Expense
import com.nickfinance.nikfinance.domain.models.Theme
import com.nickfinance.nikfinance.domain.models.AddExpenseData
import com.nickfinance.nikfinance.domain.models.AddThemeData
import com.nickfinance.nikfinance.domain.models.ExpenseWithTheme
import com.nickfinance.nikfinance.domain.repositories.abstractions.MainRepository
import com.nickfinance.nikfinance.domain.models.FilterData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val database: ExpensesDao
) : MainRepository {

    override suspend fun getExpensesWithThemes(filters: FilterData): Flow<List<ExpenseWithTheme>> =
        database.getAllExpensesWithThemes(
            minAmount = filters.minAmount,
            maxAmount = filters.maxAmount,
            minCreatedAt = filters.minCreatedAt,
            maxCreatedAt = filters.maxCreatedAt,
            isArchived = filters.isArchived
        ).map { list -> list.map { it.toExpenseWithTheme() } }

    override suspend fun getThemes(): Flow<List<Theme>> =
        database.getThemes()
            .map { list -> list.map { it.toTheme() } }

    override suspend fun updateExpense(expense: Expense) {
        database.updateExpense(expense.toExpenseDBE())
    }

    override suspend fun deleteExpense(expense: Expense) {
        database.deleteExpense(expense.toExpenseDBE())
    }


    override suspend fun saveExpense(data: AddExpenseData) {
        database.createExpense(data.toExpenseDBE())
    }

    override suspend fun saveTheme(data: AddThemeData) {
        database.createTheme(data.toThemeDBE())
    }
}