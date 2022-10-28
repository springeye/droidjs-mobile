package com.github.springeye.droidjs.base.modules

import androidx.annotation.Keep

@Keep
interface IConsole {
    fun log(vararg params:Any)
}