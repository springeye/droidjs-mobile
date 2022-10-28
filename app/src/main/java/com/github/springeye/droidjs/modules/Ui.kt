package com.github.springeye.droidjs.modules

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.github.springeye.droidjs.DroidJsApplication
import javax.inject.Inject

interface UINoteProvider{
    val rootNote:AccessibilityNodeInfo?
}
class Ui @Inject constructor(private val provider: UINoteProvider): IUi {
    val LOG_TAG="Ui"
    override fun findByText(text: String):  IUiNode? {
        Log.d(LOG_TAG,"call findByText($text)")
        val root= provider.rootNote
        if(root==null){
            Log.w(LOG_TAG,"当前没有root node")
            return null
        }
        val uiNode = root.findAccessibilityNodeInfosByText(text)
            .map { UiNode(provider,it.viewIdResourceName) }
            .firstOrNull()
        println("findByText($text) result ==>${uiNode}")
        val let = uiNode
        return let
    }



}
data class UiNode(private val provider: UINoteProvider,private val viewId:String):IUiNode{
    val LOG_TAG="UiNode"
    override fun click(){
        Log.d(LOG_TAG,"call click,viewId=$viewId")
        provider.rootNote?.findAccessibilityNodeInfosByViewId(viewId)?.first()?.apply {
            if(isClickable){
                println("点击")
                performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }else{
                println("不能点击")
            }
        }
    }
}