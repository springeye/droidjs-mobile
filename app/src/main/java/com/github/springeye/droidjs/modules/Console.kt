package com.github.springeye.droidjs.modules

import android.util.Log
import androidx.annotation.Keep
import javax.inject.Inject

@Keep
interface IConsole {
    fun log(vararg params:Any)
}
class Console @Inject constructor(): IConsole {
    override fun log(vararg params: Any) {
        Log.v("console",params.joinToString(" "))
    }
}