package com.github.springeye.droidjs.jsmodules

import android.view.accessibility.AccessibilityNodeInfo
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8Object
import com.github.springeye.droidjs.DroidJsApplication
import com.github.springeye.droidjs.v8.getV8Object
import com.github.springeye.droidjs.v8.register

interface IUi {
    fun findByText(text:String):V8Object?

}
class Ui(private val v8: V8):IUi{

    override fun findByText(text: String):V8Object? {
        val root= DroidJsApplication.root ?: return null

        val let = root.findAccessibilityNodeInfosByText(text)
            .map { UiNode(it.viewIdResourceName) }
            .firstOrNull()?.let { v8.getV8Object(it)
        }
        return let
    }



}
class UiNode(private val viewId:String){
    fun click(){
        DroidJsApplication.root?.findAccessibilityNodeInfosByViewId(viewId)?.first()?.apply {
            if(isClickable){
                println("点击")
                performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }else{
                println("不能点击")
            }
        }
    }
}