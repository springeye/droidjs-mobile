package com.github.springeye.droidjs.js

import com.github.springeye.droidjs.modules.App
import com.github.springeye.droidjs.modules.IApp

class AppJs(private val app: App): IApp by app {
}