package com.github.springeye.droidjs

import android.app.Application
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.github.springeye.droidjs.base.modules.UINoteProvider
import dagger.hilt.android.HiltAndroidApp
import org.opencv.android.OpenCVLoader

@HiltAndroidApp
class DroidJsApplication: Application(), UINoteProvider {
    var root: AccessibilityNodeInfo?=null
    companion object{
        @JvmStatic
        var app:DroidJsApplication?=null
    }
    override fun onCreate() {
        super.onCreate()
        app=this

        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV", "Unable to load OpenCV!");
        else
            Log.d("OpenCV", "OpenCV loaded Successfully!");
    }

    override val rootNote: AccessibilityNodeInfo?
        get() = root
}