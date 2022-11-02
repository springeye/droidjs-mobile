package com.github.springeye.droidjs.luaj

import android.app.Application

class DeviceLib(private val app:Application): AbstractLib() {
    override val libName: String
        get() = "device"
}