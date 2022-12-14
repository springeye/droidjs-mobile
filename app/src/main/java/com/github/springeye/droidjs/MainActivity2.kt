package com.github.springeye.droidjs

//import com.eclipsesource.v8.V8ScriptExecutionException
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.github.springeye.droidjs.base.ScriptRuntime
import com.github.springeye.droidjs.base.modules.IApp
import com.github.springeye.droidjs.ext.match
import com.github.springeye.droidjs.ui.theme.DroidjsmobileTheme
import com.github.springeye.droidjs.utils.AccessibilityServiceTool
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.opencv.core.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {
    @Inject
    lateinit var js: ScriptRuntime
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
        lifecycleScope.launch{
            async {
                runCatching {
                    if(AccessibilityServiceUtils.isAccessibilityServiceEnabled(this@MainActivity2,AppAccessibilityService::class.java)){

                    }else{
                        if(!AccessibilityServiceTool.enableAccessibilityServiceByRootAndWaitFor(this@MainActivity2,2000)){
                            throw Exception("???????????????????????????")
                        }
                    }
                }.exceptionOrNull()?.printStackTrace()
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
                println("??????????????????????????????  ${message}")
                val json= JSONObject(message)
                if(json.getString("type")=="script"){
                    val script = json.getString("content")
                    try {
                        if(json.getString("lang")=="lua"){
                            js.exec(script,ScriptRuntime.Type.LUA);
                        }else if(json.getString("lang")=="js") {
                            js.exec(script,ScriptRuntime.Type.JS);
                        }
                    } catch (e: Exception) {
                        when (e) {
                            else -> {
                                Log.e("MainActivity", "????????????????????????", e)
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
            Text("??????")
            Image(bitmap = template.asImageBitmap(), contentDescription = "??????")
            ShowImage(screenhot,template,result)
            Button(modifier = Modifier.semantics(mergeDescendants = true) {},onClick = {
                viewModel.executeTest()
            }) {
                Text("??????????????????")
            }
            Button(modifier = Modifier.semantics(mergeDescendants = true) {},onClick = {
                findBitmap()
            }) {
                Text("????????????")
            }
        }
    })

}

@Composable
fun ShowImage(target:Bitmap,template:Bitmap,result:Bitmap?) {
    Row() {
        Column(modifier = Modifier.weight(1f)) {
            Text("??????")
            Image(bitmap = target.asImageBitmap(), contentDescription = "??????")
        }
        Column(modifier = Modifier.weight(1f)) {

            Text("??????")
            if(result!=null) {
                Image(bitmap = result.asImageBitmap(), contentDescription = "??????")
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