package com.github.springeye.droidjs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.duktape.Duktape
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var js: Duktape
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val app=application as DroidJsApplication
        js.evaluate("app.launchApp('com.tencent.wx')")
        js.evaluate("console.log('js测试js测试js测试js测试js测试');")

    }
}