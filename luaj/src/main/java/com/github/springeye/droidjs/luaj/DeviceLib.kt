package com.github.springeye.droidjs.luaj

import android.app.Application
import android.os.Build
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.LibFunction

class DeviceLib(private val app: Application) : AbstractLib() {

    override val libName: String
        get() = "device"
    override val functions: Map<String, LibFunction>
        get() = mapOf()
    override val fields: Map<String, LuaValue>
        get() = mapOf(
            "width" to app.resources.displayMetrics.widthPixels.lua,
            "height" to app.resources.displayMetrics.heightPixels.lua,
            "buildId" to Build.ID.lua,
            "broad" to Build.BOARD.lua,
            "brand" to Build.BRAND.lua,
            "device" to Build.DEVICE.lua,
            "model" to Build.MODEL.lua,
            "product" to Build.PRODUCT.lua,
            "bootloader" to Build.BOOTLOADER.lua,
            "hardware" to Build.HARDWARE.lua,
            "fingerprint" to Build.FINGERPRINT.lua,
            "serial" to Build.SERIAL.lua,
            "sdkInt" to Build.VERSION.SDK_INT.lua,
            "release" to Build.VERSION.RELEASE.lua,
            "codename" to Build.VERSION.CODENAME.lua,
        )

    @Export
    fun deviceInfo(text: String): String {
        return "我是设备信息=>${text}"
    }
}