package com.nickfinance.nikfinance.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
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

    @Transaction
    @Query("SELECT * FROM expenses")
    fun getAllExpensesWithThemes(): Flow<List<ExpenseWithThemeDBE>>

    @Query("SELECT * FROM themes")
    fun getThemes(): Flow<List<ThemeDBE>>

}