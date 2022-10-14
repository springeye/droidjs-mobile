package com.github.springeye.droidjs.jsmodules

import android.util.Log
import javax.inject.Inject


interface IConsole {
    fun log(vararg params:Any)
}
class Console @Inject constructor(): IConsole {
    override fun log(vararg params: Any) {
        Log.v("console",params.joinToString(" "))
    }
}