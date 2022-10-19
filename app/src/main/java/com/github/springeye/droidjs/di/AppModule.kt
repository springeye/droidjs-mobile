package com.github.springeye.droidjs.di

import android.app.Application
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.LuaRuntime
import com.github.springeye.droidjs.modules.*
import com.github.springeye.droidjs.runtime.JSRuntimeV8
import com.github.springeye.droidjs.runtime.LuaRuntimeLuaj
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDroidJsApplication(app:Application): DroidJsApplication {
        return app as DroidJsApplication
    }
}
@Module
@InstallIn(SingletonComponent::class)
abstract class BindSingletonModule{
    @Binds
    @Singleton
    abstract fun bindJSRuntime(runtime:JSRuntimeV8):JSRuntime
    @Binds
    @Singleton
    abstract fun bindLuaRuntime(runtime:LuaRuntimeLuaj):LuaRuntime
    @Binds
    @Singleton
    abstract fun bindApp(app: App):IApp
    @Binds
    @Singleton
    abstract fun bindConsole(console:Console):IConsole
    @Binds
    @Singleton
    abstract fun bindUi(ui:Ui):IUi

}