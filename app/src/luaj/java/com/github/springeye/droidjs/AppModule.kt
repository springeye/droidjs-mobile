package com.github.springeye.droidjs

import com.github.springeye.droidjs.base.ScriptRuntime
import com.github.springeye.droidjs.base.modules.*
import com.github.springeye.droidjs.modules.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun providerScriptRuntime(impl:ScriptEngineWrapper):ScriptRuntime
}
