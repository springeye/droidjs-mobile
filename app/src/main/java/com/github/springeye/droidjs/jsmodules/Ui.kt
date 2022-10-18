package com.github.springeye.droidjs.jsmodules

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.v8.getV8Object

interface IUi {
    fun findByText(text:String):V8Object?

}
class Ui(private val app:DroidJsApplication,private val v8: V8):IUi{
    val LOG_TAG="Ui"
    override fun findByText(text: String):V8Object? {
        Log.d(LOG_TAG,"call findByText($text)")
        val root= app.root
        if(root==null){
            Log.w(LOG_TAG,"当前没有root node")
            return null
        }

        val uiNode = root.findAccessibilityNodeInfosByText(text)
            .map { UiNode(app,it.viewIdResourceName) }
            .firstOrNull()
        println("findByText($text)==>${uiNode}")
        val let = uiNode?.let { v8.getV8Object(it) }
        return let
    }



}
data class UiNode(private val app:DroidJsApplication,private val viewId:String){
    val LOG_TAG="UiNode"
    fun click(){
        Log.d(LOG_TAG,"call click,viewId=$viewId")
        app.root?.findAccessibilityNodeInfosByViewId(viewId)?.first()?.apply {
            if(isClickable){
                println("点击")
                performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }else{
                println("不能点击")
            }
        }
    }
}