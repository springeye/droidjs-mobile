package com.github.springeye.droidjs.base.modules

import androidx.annotation.Keep


@Keep
interface IApp {
    fun getApkInfo(file:String)
    fun getAppName(packageName:String):String?
    fun getPackageName(targetAppName:String):String?
    fun launch(packageName:String):Boolean
    fun launchApp(targetAppName:String):Boolean
    fun openAppSettings(packageName: String)
    fun uninstall(packageName: String)
    fun openUrl(url:String)
    val versionCode:Int
    val versionName:String
}