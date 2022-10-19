package com.github.springeye.droidjs.di

import android.app.Application
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.LuaRuntime
import com.github.springeye.droidjs.lua.LuaRuntimeLuaj
import com.github.springeye.droidjs.luamodules.App
import com.github.springeye.droidjs.v8.JSRuntimeV8
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.jse.JsePlatform
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDroidJsApplication(app:Application): DroidJsApplication {
        return app as DroidJsApplication
    }
    @Singleton
    @Provides
    fun provideJSRuntime(app:DroidJsApplication): JSRuntime {
        return JSRuntimeV8(app)
    }
    @Singleton
    @Provides
    fun provideLuaRuntime(app:DroidJsApplication): LuaRuntime {
        return LuaRuntimeLuaj(app)
    }
}