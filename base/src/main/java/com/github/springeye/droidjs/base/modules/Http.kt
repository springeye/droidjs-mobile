package com.github.springeye.droidjs.base.modules

import androidx.annotation.Keep

@Keep
interface IHttp {
    fun get(url:String)
    fun post(url:String)
}