package com.github.springeye.droidjs

import android.app.Application
import com.github.springeye.droidjs.base.ScriptRuntime
import com.github.springeye.droidjs.base.modules.*
import com.github.springeye.droidjs.j2v8.*
import com.github.springeye.droidjs.modules.*
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
    fun providerScriptRuntime(application: Application,ui:IUi,app:IApp,console:IConsole,image:IImage):ScriptRuntime{
        return ScriptEngineV8(application,ui,app,console,image)
    }
}
