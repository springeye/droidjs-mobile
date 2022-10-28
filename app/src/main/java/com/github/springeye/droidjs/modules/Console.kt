package com.github.springeye.droidjs.modules

import android.util.Log
import androidx.annotation.Keep
import com.github.springeye.droidjs.base.modules.IConsole
import javax.inject.Inject


class Console @Inject constructor(): IConsole {
    override fun log(vararg params: Any) {
        Log.v("console",params.joinToString(" "))
    }
}