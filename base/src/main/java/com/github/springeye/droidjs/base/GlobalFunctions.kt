package com.github.springeye.droidjs.base

interface GlobalFunctions {
    suspend fun alert(title:String?,message:String)
    suspend fun toast(text:String)
    suspend fun backHome()
}