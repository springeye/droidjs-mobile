package com.github.springeye.droidjs.jsmodules

import javax.inject.Inject


interface IApp {
    fun launchApp(packageName:String)
}
class App @Inject constructor(): IApp {
    override fun launchApp(packageName:String){
        println("====================launchApp:${packageName}")
    }
}