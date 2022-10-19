package com.github.springeye.droidjs.js

import com.github.springeye.droidjs.modules.Console
import com.github.springeye.droidjs.modules.IConsole

class ConsoleJs(private val console: IConsole): IConsole by console{

}