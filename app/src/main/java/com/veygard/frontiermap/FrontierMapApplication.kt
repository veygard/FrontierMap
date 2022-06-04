package com.veygard.frontiermap

import android.app.Application
import com.veygard.frontiermap.di.domainModule
import com.veygard.frontiermap.di.networkModule
import com.veygard.frontiermap.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FrontierMapApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin {
            androidContext(this@FrontierMapApplication)
            androidLogger()
            modules(networkModule, domainModule, viewModelModule)
        }
    }
}