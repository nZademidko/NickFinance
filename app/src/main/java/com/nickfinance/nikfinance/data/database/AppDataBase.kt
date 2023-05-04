package com.nickfinance.nikfinance.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import com.nickfinance.nikfinance.data.database.entity.ThemeDBE

@Database(
    version = 2,
    entities = [
        ExpenseDBE::class, ThemeDBE::class
    ],
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getExpensesDao(): ExpensesDao
}