package com.ddaypunk.aperture

import android.app.Application
import android.content.Context
import com.ddaypunk.aperture.di.initKoin
import org.koin.android.ext.koin.androidContext



class ApertureApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        initKoin {
            androidContext(this@ApertureApplication)
        }
    }

    companion object {
        var appContext: Context? = null
            private set
    }
}