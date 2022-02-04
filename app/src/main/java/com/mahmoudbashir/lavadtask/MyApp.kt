package com.mahmoudbashir.lavadtask

import android.app.Application
import com.mahmoudbashir.lavadtask.koinDI.mainViewModel
import com.mahmoudbashir.lavadtask.koinDI.newsApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // todo as its name we start Koin and pass a context , modules to its
        startKoin {
            androidContext(this@MyApp)
            modules(
                newsApi,
                mainViewModel
            )
        }
    }
}