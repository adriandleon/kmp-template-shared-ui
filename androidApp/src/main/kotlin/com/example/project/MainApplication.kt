package com.example.project

import android.app.Application
import com.example.project.common.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin { androidContext(this@MainApplication) }
    }
}
