package com.github.springeye.droidjs.di

import android.app.Application
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.base.modules.*
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

    @Provides
    @Singleton
     fun provideApp(app:Application): IApp=App(app)
    @Provides
    @Singleton
     fun provideConsole(app:Application): IConsole=Console()
    @Provides
    @Singleton
     fun provideUi(app:Application,provider:UINoteProvider):IUi=Ui(provider)
    @Provides
    @Singleton
     fun provideImage(app:Application): IImage=Image()
}