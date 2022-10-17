package com.github.springeye.droidjs

import android.os.Bundle
import android.view.View
import android.widget.Button
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
//        val script=assets.open("tests/app.js").bufferedReader().use {
//            it.readText()
//        }
//        js.exec(script)
        findViewById<Button>(R.id.btn_find).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        println( app.root?.packageName)
    }
}