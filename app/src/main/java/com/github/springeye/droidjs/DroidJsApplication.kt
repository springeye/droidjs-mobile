package com.github.springeye.droidjs

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DroidJsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

    }
}