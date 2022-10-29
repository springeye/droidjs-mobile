package com.github.springeye.droidjs.j2v8

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.modules.IUi

open class UiJs(private val ui: IUi, private val v8:V8): JsModule {
    fun findByText(text: String): V8Object? {
        val uiNode = ui.findByText(text)
        return uiNode?.toV8Object(v8)
    }
}