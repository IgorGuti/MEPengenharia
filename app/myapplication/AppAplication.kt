package com.example.myapplication

import android.app.Application
import android.util.Log
import android.widget.Toast
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
class AppAplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppAplication)
            modules(
                listOf(
                )
            )
        }
    }
}