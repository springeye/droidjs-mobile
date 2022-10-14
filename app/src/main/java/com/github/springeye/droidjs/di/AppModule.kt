package com.github.springeye.droidjs.di

import com.github.springeye.droidjs.jsmodules.App
import com.github.springeye.droidjs.jsmodules.Console
import com.github.springeye.droidjs.jsmodules.IApp
import com.github.springeye.droidjs.jsmodules.IConsole
import com.squareup.duktape.Duktape
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
    fun provideDuktape(): Duktape {
        val duktape = Duktape.create()
        val console= Console()
        val app= App()
        duktape.set("console", IConsole::class.java,console)
        duktape.set("app", IApp::class.java,app)
        return duktape
    }
}