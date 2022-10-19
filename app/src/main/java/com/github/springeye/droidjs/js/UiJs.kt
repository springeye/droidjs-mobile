package com.github.springeye.droidjs.js

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.modules.IUi
import getV8Object

class UiJs(private val ui: IUi, private val v8:V8){
    fun findByText(text: String): V8Object? {
        return ui.findByText(text)?.let { v8.getV8Object(it) }
    }
}