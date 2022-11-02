package com.github.springeye.droidjs.base

import android.view.KeyEvent

interface GlobalFunctions {
    suspend fun alert(title:String?,message:String)
    suspend fun toast(text:String)
    suspend fun backHome()
    suspend fun sendKey(keyCode:Int)
    suspend fun sendText(text:String)
}