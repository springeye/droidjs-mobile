package com.github.springeye.droidjs

import android.accessibilityservice.AccessibilityService
import android.app.AlertDialog
import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.github.springeye.droidjs.base.GlobalFunctions
import com.github.springeye.droidjs.base.modules.UINoteProvider
import dagger.hilt.android.HiltAndroidApp
import org.opencv.android.OpenCVLoader

@HiltAndroidApp
class DroidJsApplication : Application(), UINoteProvider, GlobalFunctions {
    var root: AccessibilityNodeInfo? = null
    var service:AppAccessibilityService?=null
    companion object {
        @JvmStatic
        var app: DroidJsApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        if (!OpenCVLoader.initDebug())
            Log.e("OpenCV", "Unable to load OpenCV!");
        else
            Log.d("OpenCV", "OpenCV loaded Successfully!");
    }

    override val rootNote: AccessibilityNodeInfo?
        get() = root

    override fun alert(title: String?, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title?:"")
            .setMessage(message)
            .create().apply {
                window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            }.show()
    }

    override fun toast(text: String) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    override fun backHome() {
        service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }
}