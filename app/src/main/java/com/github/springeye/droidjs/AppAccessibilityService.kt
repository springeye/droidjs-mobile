package com.github.springeye.droidjs

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppAccessibilityService: AccessibilityService() {
    @Inject
    lateinit var application: DroidJsApplication
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        application.root = event?.source
        if(application.root==null)return
    }

    override fun onInterrupt() {

    }
}