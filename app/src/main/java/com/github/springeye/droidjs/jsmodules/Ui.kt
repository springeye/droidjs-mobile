package com.github.springeye.droidjs.jsmodules

import com.github.springeye.droidjs.DroidJsApplication

interface IUi {
    fun findByText(text:String)
    fun findByTexts(text:String)
}
class Ui(private val app:DroidJsApplication):IUi{
    val root=app.root
    override fun findByText(text: String) {
        if(root==null)return
    }

    override fun findByTexts(text: String) {
        if(root==null)return
    }

}
class UiNode{
    fun click(){

    }
}