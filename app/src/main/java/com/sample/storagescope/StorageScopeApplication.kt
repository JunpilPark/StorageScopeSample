package com.sample.storagescope

import android.app.Application
import com.sample.storagescope.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StorageScopeApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@StorageScopeApplication)
            modules(appModule)
        }
    }
}