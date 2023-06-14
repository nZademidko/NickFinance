package com.nickfinance.nikfinance

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        private lateinit var instance: App
        fun get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MapKitFactory.setApiKey("00fb6364-2da0-46f3-8626-6f90207a03e1")
    }
}