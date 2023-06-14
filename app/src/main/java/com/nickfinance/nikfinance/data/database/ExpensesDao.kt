package com.nickfinance.nikfinance.data.database

import androidx.room.*
import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import com.nickfinance.nikfinance.data.database.entity.ExpenseWithThemeDBE
import com.nickfinance.nikfinance.data.database.entity.ThemeDBE
import kotlinx.coroutines.flow.Flow


//language=sql
@Dao
interface ExpensesDao {

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<ExpenseDBE?>

    @Insert
    suspend fun createExpense(expenseDBE: ExpenseDBE)

    @Insert
    suspend fun createTheme(themeDBE: ThemeDBE)

    @Transaction
    @Query(
        "SELECT * FROM expenses WHERE " +
                "(:minAmount IS NULL OR amount >= :minAmount) AND " +
                "(:maxAmount IS NULL OR amount <= :maxAmount) AND" +
                "(:minCreatedAt IS NULL OR created_at >= :minCreatedAt) AND " +
                "(:maxCreatedAt IS NULL OR created_at <= :maxCreatedAt) AND" +
                "(:isArchived = 1 OR is_archived = 0)" +
                "ORDER BY created_at DESC"
    )
    fun getAllExpensesWithThemes(
        minAmount: Double?,
        maxAmount: Double?,
        minCreatedAt: Long?,
        maxCreatedAt: Long?,
        isArchived: Boolean
    ): Flow<List<ExpenseWithThemeDBE>>

    @Query("SELECT * FROM themes")
    fun getThemes(): Flow<List<ThemeDBE>>

    @Update
    fun updateExpense(expenseDBE: ExpenseDBE)

    @Delete
    fun deleteExpense(expenseDBE: ExpenseDBE)
}