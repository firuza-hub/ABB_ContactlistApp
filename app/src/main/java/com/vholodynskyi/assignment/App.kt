package com.vholodynskyi.assignment

import android.app.Application
import com.vholodynskyi.assignment.di.appModule
import com.vholodynskyi.assignment.di.repoModule
import com.vholodynskyi.assignment.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, repoModule, viewModelModule))
        }
    }
}