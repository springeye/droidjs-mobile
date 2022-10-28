import com.github.springeye.droidjs.ScriptRuntime
import com.github.springeye.droidjs.runtime.ScriptEngineLuaj
import com.github.springeye.droidjs.runtime.ScriptEngineV8
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
    abstract fun bindV8Runtime(runtime: ScriptEngineLuaj): ScriptRuntime
}