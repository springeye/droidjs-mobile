package com.github.springeye.droidjs.js

import com.github.springeye.droidjs.base.modules.IApp

class AppJs(private val app: IApp):JsModule, IApp by app {
}