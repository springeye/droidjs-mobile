package com.github.springeye.droidjs.base.modules

import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.github.springeye.droidjs.modules.IUi
import com.github.springeye.droidjs.modules.IUiNode

interface UINoteProvider{
    val rootNote:AccessibilityNodeInfo?
}
class Ui  constructor(private val provider: UINoteProvider): IUi {
    val LOG_TAG="Ui"
    override fun findByText(text: String): IUiNode? {
        Log.d(LOG_TAG, "call findByText($text)")
        val root = provider.rootNote
        if (root == null) {
            Log.w(LOG_TAG, "当前没有root node")
            return null
        }
        val uiNode = root.findAccessibilityNodeInfosByText(text)
            .map {
                Log.d(LOG_TAG, "查找到节点：${it}")
                UiNode(provider, it)
            }
            .firstOrNull()
        println("findByText($text) result ==>${uiNode}")
        return uiNode
    }



}
data class UiNode(private val provider: UINoteProvider, private val node: AccessibilityNodeInfo): IUiNode {
    val LOG_TAG="UiNode"
    override fun click(){
        Log.d(LOG_TAG,"call click,viewId=${node.viewIdResourceName},text:${node.text}")
        node.apply {
            if(isClickable){
                println("点击")
                performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }else{
                println("不能点击")
            }
        }
    }
}