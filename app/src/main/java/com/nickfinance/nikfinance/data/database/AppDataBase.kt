package com.nickfinance.nikfinance.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.nickfinance.nikfinance.data.database.entity.ExpenseDBE
import com.nickfinance.nikfinance.data.database.entity.ThemeDBE

@Database(
    version = 7,
    autoMigrations = [
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7)
    ],
    entities = [
        ExpenseDBE::class, ThemeDBE::class
    ],
    exportSchema = true
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getExpensesDao(): ExpensesDao
}