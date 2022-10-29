package com.github.springeye.droidjs.base.modules

import android.util.Log


class Console  constructor(): IConsole {
    override fun log(vararg params: Any) {
        Log.v("console",params.joinToString(" "))
    }
}