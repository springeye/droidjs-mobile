package com.github.springeye.droidjs

import android.app.Application
import android.view.accessibility.AccessibilityNodeInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DroidJsApplication: Application() {
    companion object{
        @JvmStatic
        var root: AccessibilityNodeInfo?=null
    }
    override fun onCreate() {
        super.onCreate()

    }
}