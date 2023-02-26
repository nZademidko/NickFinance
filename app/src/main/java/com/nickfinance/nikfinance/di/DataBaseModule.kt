package com.nickfinance.nikfinance.di

import android.content.Context
import androidx.room.Room
import com.nickfinance.nikfinance.data.database.AppDataBase
import com.nickfinance.nikfinance.data.database.ExpensesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext applicationContext: Context): AppDataBase =
        Room.databaseBuilder(
            applicationContext, AppDataBase::class.java, "database.db"
        ).createFromAsset("database/initialdb.db")
            .build()

    @Singleton
    @Provides
    fun provideDao(appDataBase: AppDataBase): ExpensesDao = appDataBase.getExpensesDao()
}