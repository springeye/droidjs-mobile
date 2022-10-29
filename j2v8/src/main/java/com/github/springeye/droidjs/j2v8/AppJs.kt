package com.github.springeye.droidjs.j2v8

import com.github.springeye.droidjs.base.modules.IApp


open class AppJs(private val app: IApp): JsModule, IApp by app {
}