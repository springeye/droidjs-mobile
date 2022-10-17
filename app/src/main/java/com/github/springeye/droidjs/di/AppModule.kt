package com.github.springeye.droidjs.di

import android.app.Application
import com.github.springeye.droidjs.DroidJsApplication
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
    fun provideDroidJsApplication(app:Application): DroidJsApplication {
        return app as DroidJsApplication
    }
    @Singleton
    @Provides
    fun provideDuktape(app:App): Duktape {
        val duktape = Duktape.create()
        val console= Console()
        duktape.set("console", IConsole::class.java,console)
        duktape.set("app", IApp::class.java,app)
        return duktape
    }
}