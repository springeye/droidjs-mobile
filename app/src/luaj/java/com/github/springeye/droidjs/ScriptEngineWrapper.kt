package com.github.springeye.droidjs

import android.app.Application
import com.github.springeye.droidjs.base.modules.IApp
import com.github.springeye.droidjs.base.modules.IConsole
import com.github.springeye.droidjs.luaj.ScriptEngineLuaj
import com.github.springeye.droidjs.modules.IUi
import javax.inject.Inject

class ScriptEngineWrapper @Inject constructor(application: Application, ui: IUi, app: IApp, console: IConsole) :
    ScriptEngineLuaj(application, ui, app, console) {
}