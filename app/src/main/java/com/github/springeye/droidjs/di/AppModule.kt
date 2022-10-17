package com.github.springeye.droidjs.di

import android.app.Application
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.JSRuntime
import com.github.springeye.droidjs.v8.JSRuntimeV8
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
    fun provideJSRuntime(app:DroidJsApplication): JSRuntime {
        return JSRuntimeV8(app)
    }
}