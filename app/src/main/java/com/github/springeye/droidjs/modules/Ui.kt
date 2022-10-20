package com.github.springeye.droidjs.modules

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.github.springeye.droidjs.DroidJsApplication
import javax.inject.Inject

interface IUi {
    fun findByText(text:String): UiNode?

}
class Ui @Inject constructor(private val app:DroidJsApplication): IUi {
    val LOG_TAG="Ui"
    override fun findByText(text: String): UiNode? {
        Log.d(LOG_TAG,"call findByText($text)")
        val root= app.root
        if(root==null){
            Log.w(LOG_TAG,"当前没有root node")
            return null
        }
        val uiNode = root.findAccessibilityNodeInfosByText(text)
            .map { UiNode(app,it.viewIdResourceName) }
            .firstOrNull()
        println("findByText($text) result ==>${uiNode}")
        val let = uiNode
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