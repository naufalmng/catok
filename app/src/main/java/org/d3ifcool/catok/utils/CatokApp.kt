package org.d3ifcool.catok.utils

import android.app.Application
import org.d3ifcool.catok.core.di.appModule
import org.d3ifcool.catok.core.di.repositoryModule
import org.d3ifcool.catok.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CatokApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CatokApp)
            modules(listOf(appModule, viewModelModule, repositoryModule))
        }
    }
}