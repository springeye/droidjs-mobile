package com.github.springeye.droidjs.di
import android.app.Application
import com.github.springeye.droidjs.base.ScriptRuntime
import com.github.springeye.droidjs.nodejs.NodeScriptRuntime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  NodeJSModule {
    @Singleton
    @Provides
    fun provideDroidJsApplication(app: Application): ScriptRuntime {
        return NodeScriptRuntime()
    }
}