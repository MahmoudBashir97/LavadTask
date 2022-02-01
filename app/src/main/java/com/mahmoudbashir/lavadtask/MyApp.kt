package com.mahmoudbashir.lavadtask

import android.app.Application
import com.mahmoudbashir.lavadtask.koinDI.mainViewModel
import com.mahmoudbashir.lavadtask.koinDI.newsApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                newsApi,
                mainViewModel
            )
        }
    }
}