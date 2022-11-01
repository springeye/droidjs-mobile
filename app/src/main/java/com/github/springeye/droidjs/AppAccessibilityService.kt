package com.github.springeye.droidjs

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import javax.inject.Inject

@AndroidEntryPoint
class AppAccessibilityService: AccessibilityService() {
    @Inject
    lateinit var application: DroidJsApplication
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val source = event?.source ?: return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            application.root = source
            Log.d("AppAccessibilityService", "onAccessibilityEvent=>${event.toString()}")
            //获取当前窗口activity名
            val componentName = ComponentName(
                event.packageName.toString(),
                event.className.toString()
            );
            try {
                var activityName = packageManager.getActivityInfo(componentName, 0).toString();
                activityName =
                    activityName.substring(activityName.indexOf(" "), activityName.indexOf("}"));
                Log.d("AppAccessibilityService", "当前窗口activity=================$activityName");
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace();

            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("AppAccessibilityService", "onServiceConnected")
        LOCK.lock()
        ENABLED.signalAll()
        LOCK.unlock()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("AppAccessibilityService", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        instance=null
        super.onDestroy()
    }
    companion object{
        private val LOCK = ReentrantLock()
        private val ENABLED = LOCK.newCondition()
        var instance:AppAccessibilityService?=null
            private set
        fun disable():Boolean{
            instance?.disableSelf()
            return true
        }
        fun waitForEnabled(timeOut: Long): Boolean {
            if (instance != null)
                return true
            LOCK.lock()
            try {
                if (instance != null)
                    return true
                if (timeOut == -1L) {
                    ENABLED.await()
                    return true
                }
                return ENABLED.await(timeOut, TimeUnit.MILLISECONDS)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                return false
            } finally {
                LOCK.unlock()
            }
        }
    }

}