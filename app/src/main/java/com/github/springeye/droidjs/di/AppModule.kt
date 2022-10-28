package com.github.springeye.droidjs.di

import android.app.Application
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.base.modules.IApp
import com.github.springeye.droidjs.base.modules.IConsole
import com.github.springeye.droidjs.base.modules.IImage
import com.github.springeye.droidjs.modules.*
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
    @Singleton
    @Provides
    fun provideUINoteProvider(app:DroidJsApplication): UINoteProvider {
        return app
    }

}
@Module
@InstallIn(SingletonComponent::class)
abstract class BindSingletonModule{
    @Binds
    @Singleton
    abstract fun bindApp(app: App): IApp
    @Binds
    @Singleton
    abstract fun bindConsole(console: Console): IConsole
    @Binds
    @Singleton
    abstract fun bindUi(ui:Ui):IUi
    @Binds
    @Singleton
    abstract fun bindImage(image: Image): IImage

}