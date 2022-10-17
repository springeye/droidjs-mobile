package com.github.springeye.droidjs

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {
    @Inject
    lateinit var js: JSRuntime
    @Inject
    lateinit var app:DroidJsApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_find).setOnClickListener(this)
        findViewById<Button>(R.id.btn_text_button).setOnClickListener {
            Toast.makeText(this,"测试按钮被点击了",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        try {
            val script=assets.open("tests/app.js").bufferedReader().use {
        it.readText()
    }
            js.exec(script)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}