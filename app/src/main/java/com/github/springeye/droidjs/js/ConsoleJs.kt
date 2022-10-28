package com.github.springeye.droidjs.js

import com.github.springeye.droidjs.base.modules.IConsole

class ConsoleJs(private val console: IConsole):JsModule, IConsole by console{

}