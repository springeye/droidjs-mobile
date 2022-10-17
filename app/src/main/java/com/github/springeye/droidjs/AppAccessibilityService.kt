package com.github.springeye.droidjs

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppAccessibilityService: AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        DroidJsApplication.root = event?.source
        if(DroidJsApplication.root==null)return
    }

    override fun onInterrupt() {

    }
}