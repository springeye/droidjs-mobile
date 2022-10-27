package com.github.springeye.droidjs

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.github.springeye.droidjs.ext.match
import com.github.springeye.droidjs.modules.IApp
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
import org.opencv.android.Utils
import org.opencv.core.*
import javax.inject.Inject

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
    var mRgb:Mat?=null
    var mTemplate:Mat?=null


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
    val context= LocalContext.current
    var screenhot:Bitmap by remember {
        mutableStateOf(BitmapFactory.decodeStream(context.assets.open("screenhot.jpeg")).let { it.copy(it.config,true) })
    }
    var template:Bitmap by remember {
                mutableStateOf(BitmapFactory.decodeStream(context.assets.open("target.png")).let { it.copy(it.config,true) })
//        mutableStateOf(BitmapFactory.decodeStream(context.assets.open("target2.png")).let { it.copy(it.config,true) })
    }
    var result:Bitmap? by remember {
        mutableStateOf(null)
    }
    fun findBitmap(){
        result=screenhot.match(template)
//        template.recycle()
//        target.recycle()
    }
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "App")
        })
    }, content = {
        Column(
            Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())) {
            Text(text = "Hello $name!")
            Text("小图")
            Image(bitmap = template.asImageBitmap(), contentDescription = "小图")
            ShowImage(screenhot,template,result)
            Button(modifier = Modifier.semantics(mergeDescendants = true) {},onClick = {
                viewModel.executeTest()
            }) {
                Text("执行本地代码")
            }
            Button(modifier = Modifier.semantics(mergeDescendants = true) {},onClick = {
                findBitmap()
            }) {
                Text("匹配图片")
            }
        }
    })

}

@Composable
fun ShowImage(target:Bitmap,template:Bitmap,result:Bitmap?) {
    Row() {
        Column(modifier = Modifier.weight(1f)) {
            Text("大图")
            Image(bitmap = target.asImageBitmap(), contentDescription = "大图")
        }
        Column(modifier = Modifier.weight(1f)) {

            Text("结果")
            if(result!=null) {
                Image(bitmap = result.asImageBitmap(), contentDescription = "结果")
            }
        }
    }


}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DroidjsmobileTheme {

            Greeting("Android")

    }
}