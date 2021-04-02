package com.blackpineapple.ziptzopt.application

import android.app.Application
import de.hdodenhof.circleimageview.BuildConfig
import timber.log.Timber

class ZiptZoptApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}