package com.nickfinance.nikfinance

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        private lateinit var instance: App
        fun get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}