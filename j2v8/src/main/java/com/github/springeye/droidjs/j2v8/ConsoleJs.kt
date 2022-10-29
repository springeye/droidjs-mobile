package com.github.springeye.droidjs.j2v8

import com.github.springeye.droidjs.base.modules.IConsole

class ConsoleJs(private val console: IConsole): JsModule, IConsole by console{

}