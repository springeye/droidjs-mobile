import com.github.springeye.droidjs.ScriptRuntime
import com.github.springeye.droidjs.di.ScriptEngineV8
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    @Singleton
    abstract fun bindV8Runtime(runtime: ScriptEngineV8): ScriptRuntime
}