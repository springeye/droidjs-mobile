package com.github.springeye.droidjs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.eclipsesource.v8.V8ScriptExecutionException
import com.github.springeye.droidjs.modules.IApp
import com.github.springeye.droidjs.proto.ProtoMessage
import com.github.springeye.droidjs.ui.theme.DroidjsmobileTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.luaj.vm2.LuaError
import javax.inject.Inject
import kotlin.io.readText

@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {
    @Inject
    lateinit var js: JSRuntime
    @Inject
    lateinit var lua: LuaRuntime
    @Inject
    lateinit var application: DroidJsApplication
    @Inject
    lateinit var app: IApp
    @Inject
    lateinit var app1: IApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DroidjsmobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        lifecycleScope.launch {
            async {
                try {
                    websocketClient()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    private suspend fun websocketClient() {
        val client = HttpClient {
            install(WebSockets)
        }
        client.webSocket(
            method = HttpMethod.Get,
            host = "192.168.60.14",
            port = 8080,
            path = "/api/v1/public/ws"
        ) {
            while (true) {
                val frame = incoming.receive() as? Frame.Text? ?: continue
                val message = frame.readText()
                println("收到服务器推送的指令  ${message}")
                val json= JSONObject(message)
                if(json.getString("type")=="script"){
                    val script = json.getString("content")
                    try {
                        if(json.getString("lang")=="lua"){
                            lua.exec(script);
                        }else if(json.getString("lang")=="js") {
                            js.exec(script);
                        }
                    } catch (e: Exception) {
                        when (e) {
                            is LuaError -> {
                                Log.e("MainActivity","执行脚本出错:\n${e.message?:""}")
                            }
                            is V8ScriptExecutionException -> {
                                Log.e("MainActivity","执行脚本出错:\n${e.message}\n${e.sourceLine}\n${e.jsStackTrace}"+"\n\n")
                            }
                            else -> {
                                Log.e("MainActivity", "执行脚本出现错误", e)
                            }
                        }
                    }

                }
            }
        }
        client.close()
        println("Connection closed. Goodbye!")
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String,viewModel:MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "App")
        })
    }, content = {
        Column(Modifier.padding(it)) {
            Text(text = "Hello $name!")
            Button(onClick = {
                viewModel.executeTest()
            }) {
                Text("测试")
            }
        }
    })

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DroidjsmobileTheme {

            Greeting("Android")

    }
}