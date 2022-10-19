package com.github.springeye.droidjs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eclipsesource.v8.V8ScriptExecutionException
import com.github.springeye.droidjs.luamodules.App
import com.github.springeye.droidjs.proto.ProtoMessage
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaError
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.jse.JsePlatform
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var js: JSRuntime
    @Inject
    lateinit var lua: LuaRuntime
    @Inject
    lateinit var app: DroidJsApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_find).setOnClickListener(this)
        findViewById<Button>(R.id.btn_text_button).setOnClickListener {
            Toast.makeText(this, "测试按钮被点击了", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            async {
                websocketClient()
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
                val json=JSONObject(message)
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
    override fun onClick(v: View?) {


        try {
            ProtoMessage.RegisterRequest.newBuilder().setEmail("").setUsername("").setPassword("").build()
            val script = assets.open("tests/app.js").bufferedReader().use {
                it.readText()
            }
            js.exec(script)
        } catch (e: Exception) {
            if(e is V8ScriptExecutionException){
                Log.w("MainActivity","${e.message}\n${e.sourceLine}\n${e.jsStackTrace}"+"\n\n")
//                e.printStackTrace()
            }else {
                Log.e("MainActivity", "执行js出现错误", e)
            }
        }
    }
}